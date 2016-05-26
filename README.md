**CREATING USER:**
curl -X POST http://localhost:8081/acl/accounts/create -H "Content-Type: application/json" -d "{""enabled"":true,""suspended"":false,""login"":""ivanov111"", ""email"":""ivanov@gmail.com"", ""name"":""Ivan"", ""secondName"":""Ivanov"", ""accountType"":""user"", ""roles"":[], ""groups"":[""5720a6bf56d06b2bbf907230""], ""permissions"":[], ""info"":""some info"", ""hash"":""123456789"", ""sessionTime"":15}"
curl -X POST http://localhost:8081/acl/accounts/create -H "Content-Type: application/json" -d "{""enabled"":true,""suspended"":false,""login"":""ivanov"", ""email"":""ivanov@gmail.com"", ""name"":""Ivan"", ""secondName"":""Ivanov"", ""accountType"":""user"", ""roles"":[], ""groups"":[], ""permissions"":[], ""info"":""some info"", ""hash"":""123456789"", ""sessionTime"":15}"

| Answers                                                  |
-----------------------------------------------------------|
| {"code":200,"message":"Success"}                         |
| {"code":400,"message":"Invalid JSON"}                    |
| {"code":404,"message":"Someone already has that login"}  |
| {"code":400,"message":"Error"}                           |


**UPDATING USER'S INFORMATION:**
curl -X POST http://localhost:8081/acl/accounts/update -H "Content-Type: application/json" -d "{""id"":""571df9d956d06b2bbf905a83"", ""enabled"":false,""suspended"":false,""login"":""new__ivanov"", ""accountType"":""user"", ""hash"":""12345678910"", ""sessionTime"":20}"
curl -X POST http://localhost:8081/acl/accounts/update -H "Content-Type: application/json" -d "{""id"":""570d00e311ad42174d4cac2a"",  ""login"":""ivanova1223""}"

| Answers                                                    |
-------------------------------------------------------------|
| {"code":200,"message":"Success"}                           |
| {"code":400,"message":"Invalid JSON"}                      |
| {"code":404,"message":"Someone already has that login"}    | 
| {"code":400,"message":"Error"}                             |
| {"code":404,"message":"This id doesn't match any document"}|

**UPDATING PASSWORD:**
curl -X POST http://localhost:8081/acl/accounts/update_password -H "Content-Type: application/json" -d "{""id"":""570d00e311ad42174d4cac2a"", ""hash"":""123456789104564646""}"

| Answers                                                    |
-------------------------------------------------------------|
| {"code":200,"message":"Success"}                           |
| {"code":400,"message":"Invalid JSON"}                      |
| {"code":404,"message":"Someone already has that login"}    | 
| {"code":400,"message":"Error"}                             |
| {"code":404,"message":"This id doesn't match any document"}|

**UPDATING PASSWORD BY USER:**
curl -X POST http://localhost:8081/acl/accounts/profile/update_password -H "Content-Type: application/json" -d "{""login"":""ivanov"", ""oldPassword"":""123456789"", ""newPassword"":""1092388848484888""}"

| Answers                                                      |
---------------------------------------------------------------|
| {"code":200,"message":"Success"}                             |
| {"code":400,"message":"Invalid JSON"}                        |
| {"code":400,"message":"Error"}                               |
| {"code":404,"message":"This login doesn't match any account"}|
| {"code":404,"message":"Wrong password"}                      |

**UPDATING USER'S INFORMATION BY USER:**
curl -X POST http://localhost:8081/acl/accounts/profile/update_info -H "Content-Type: application/json" -d "{""login"":""ivanov"",""hash"":""1092388848484888"", ""email"":""ivanov123@gmail.com"", ""name"":""Ivanij"", ""secondName"":""Ivanov"", ""info"":[""newInfo""]}"

| Answers                                                      |
---------------------------------------------------------------|
| {"code":200,"message":"Success"}                             |
| {"code":400,"message":"Invalid JSON"}                        |
| {"code":400,"message":"Error"}                               |
| {"code":404,"message":"This login doesn't match any account"}|
| {"code":404,"message":"Wrong password"}                      |

**ENABLE USER:**
curl -X POST http://localhost:8081/acl/accounts/enable -H "Content-Type: application/json" -d "{""id"":""570d00e311ad42174d4cac2a""}"

| Answers                                                      |
---------------------------------------------------------------|
| {"code":200,"message":"Success"}                             |
| {"code":400,"message":"Invalid JSON"}                        |
| {"code":400,"message":"Error"}                               |
| {"code":404,"message":"This id doesn't match any document"}  |


**DISABLE USER:**
curl -X POST http://localhost:8081/acl/accounts/disable -H "Content-Type: application/json" -d "{""id"":""570d00e311ad42174d4cac2a""}"

| Answers                                                      |
---------------------------------------------------------------|
| {"code":200,"message":"Success"}                             |
| {"code":400,"message":"Invalid JSON"}                        |
| {"code":400,"message":"Error"}                               |
| {"code":404,"message":"This id doesn't match any document"}  |

**SUSPEND USER:**
curl -X POST http://localhost:8081/acl/accounts/suspend -H "Content-Type: application/json" -d "{""id"":""570d00e311ad42174d4cac2a""}"

| Answers                                                      |
---------------------------------------------------------------|
| {"code":200,"message":"Success"}                             |
| {"code":400,"message":"Invalid JSON"}                        |
| {"code":400,"message":"Error"}                               |
| {"code":404,"message":"This id doesn't match any document"}  |

**UNSUSPEND USER:**
curl -X POST http://localhost:8081/acl/accounts/unsuspend -H "Content-Type: application/json" -d "{""id"":""571a30bd11ad42174d4cf90a""}"

| Answers                                                      |
---------------------------------------------------------------|
| {"code":200,"message":"Success"}                             |
| {"code":400,"message":"Invalid JSON"}                        |
| {"code":400,"message":"Error"}                               |
| {"code":404,"message":"This id doesn't match any document"}  |

**COMPLETE LIST OF USERS WITH THE ABILITY TO FILTER THEM:**
curl -X POST http://localhost:8081/acl/accounts/list -H "Content-Type: application/json" -d "{""id"":"""", ""enabled"":"""",""suspended"":"""",""login"":"""", ""email"":"""", ""name"":"""", ""secondName"":"""", ""accountType"":"""", ""roles"":[], ""groups"":[], ""permissions"":[], ""info"":[], ""created"":[], ""sessionTime"":[]}"

| Rules                                                                              |
-------------------------------------------------------------------------------------|
|login, name, secondName, info: search by first symbols of the word or the whole word|
|created, sessionTime: search by range                                               | 

| Answers                                                      |
---------------------------------------------------------------|
| {"code":200,"message":[{"secondName":"Ivanov","name":"Ivan","email":"ivanov@gmail.com","enabled":"false","suspended":"false","permissions":["ACL.Roles.Write.Create","ACL.Roles.Write.Update"],"sessionTime":"20","groups":["57027591589240e69112e97e"],"info":["some info"],"id":"571a30bd11ad42174d4cf90a","roles":["5702759b589240e69112e97f"],"accountType":"user","login":"new__ivanov","created":"2016-04-22T14:09:21"}]}|
| {"code":400,"message":"Invalid JSON"}                        |
| {"code":400,"message":"Error"}                               |

**LIST OF ACCOUNT PERMISSIONS:**
curl -X POST http://localhost:8081/acl/accounts/permissions -H "Content-Type: application/json" 

| Answers                                                       |
----------------------------------------------------------------|
| {"provide":["ACL.Accounts.Grant","ACL.Accounts.Write.Create","ACL.Accounts.Write.UpdatePassword", "ACL.Accounts.Write.UpdateUserInfo", "ACL.Accounts.Write.UpdateUsersGroup", "ACL.Accounts.Write.UpdateUsersRole", "ACL.Accounts.Write.Suspend", "ACL.Accounts.Write.Enable", "ACL.Accounts.Write.Disable", "ACL.Accounts.Read.List", "ACL.Accounts.Read.GetById", "ACL.Accounts.Read.SessionTime"], "required":["ACL.Cached.Write.Update","ACL.Permissions.Read.List","ACL.Roles.Read.List","ACL.Groups.Read.List"]}|
| {"code":400,"message":"Invalid JSON"}                        |
| {"code":400,"message":"Error"}                               |

**SESSION TIME OF ACCOUNT:**
curl -X POST http://localhost:8081/acl/accounts/session_time -H "Content-Type: application/json" -d "{""login"":""new__ivanov""}"

| Answers                                                      |
---------------------------------------------------------------|
| {"code":200,"message":"20"}                                  |
| {"code":400,"message":"Invalid JSON"}                        |
| {"code":400,"message":"Error"}                               |
| {"code":404,"message":"This login doesn't match any account"}|

**ADDING ACCOUNT TO GROUP:**
curl -X POST http://localhost:8081/acl/accounts/insert_group -H "Content-Type: application/json" -d "{""id"":""571a30bd11ad42174d4cf90a"",""idGroup"":""5703ca1ad6247cf1dfdd0e5a""}"

| Answers                                                      |
---------------------------------------------------------------|
| {"code":200,"message":"Success"}                             |
| {"code":400,"message":"Invalid JSON"}                        |
| {"code":400,"message":"Error"}                               |
| {"code":404,"message":"This id doesn't match any document"}  |

**DELETE ROLE:**
curl -X POST http://localhost:8081/acl/roles/delete -H "Content-Type: application/json" -d "{""id"":""5702759b589240e69112e97f""}"

| Answers                                                      |
---------------------------------------------------------------|
| {"code":200,"message":"Success"}                             |
| {"code":400,"message":"Invalid JSON"}                        |
| {"code":400,"message":"Error"}                               |
| {"code":404,"message":"This id doesn't match any document"}  |

**CREATE ROLE**:
curl -X POST http://localhost:8081/acl/roles/create -H "Content-Type: application/json" -d "{""name"":""Admins"",""description"":""Some roles of admin"",""permissions"":[""ACL.Permissions.Read.ListPermissions""]}"

| Answers                                                      |
---------------------------------------------------------------|
| {"code":200,"message":"Success"}                             |
| {"code":400,"message":"Invalid JSON"}                        |
| {"code":400,"message":"Error"}                               |
| {"code":404,"message":"Record already exist"}                |

**UPDATE ROLE**:
curl -X POST http://localhost:8081/acl/roles/update -H "Content-Type: application/json" -d "{""id"":""57164e8611ad42174d4ce46c"",""name"":""UpdatedAdmin"",""description"":""Some roles of admin"",""permissions"":[""ACL.Roles.Write.Create""]}"

| Answers                                                      |
---------------------------------------------------------------|
| {"code":200,"message":"Success"}                             |
| {"code":400,"message":"Invalid JSON"}                        |
| {"code":400,"message":"Error"}                               |
| {"code":404,"message":"This id doesn't match any document"}  |

**GET ROLE:**  
curl -X POST http://localhost:8081/acl/roles/get -H "Content-Type: application/json" -d "{""id"":""571a40b011ad42174d4cf949""}"  

| Answers                                                      |
---------------------------------------------------------------|
| {"code":200,"message":{"id":"571a40b011ad42174d4cf949","name":"Admins", "description":"Some roles of admin", "permissions":["ACL.Roles.Write.Create","ACL.Roles.Write.Update"]}}|
| {"code":400,"message":"Invalid JSON"}                        |
| {"code":400,"message":"Error"}                               |
| {"code":404,"message":"This id doesn't match any document"}  |

**LIST OF ROLES:**  
curl -X POST http://localhost:8081/acl/roles/list -H "Content-Type: application/json" -d "{}"

| Answers                                                      |
---------------------------------------------------------------|
| {"code":200,"message":{"roles":[{"id":"571a40b011ad42174d4cf949","name":"Admins", "description":"Some roles of admin", "permissions":["ACL.Roles.Write.Create","ACL.Roles.Write.Update"]}]}}| 
| {"code":400,"message":"Invalid JSON"}                        |                     
| {"code":400,"message":"Error"}                               |

**PERMISSIONS OF ROLES:**
curl -X POST http://localhost:8081/acl/roles/permissions -H "Content-Type: application/json" -d "{}"

| Answers                                                      |
|--------------------------------------------------------------
| {"provide":["ACL.Roles.Read.List", "ACL.Roles.Read.Get", "ACL.Roles.Write.Create", "ACL.Roles.Write.Update","ACL.Roles.Write.Delete"], "required":["ACL.Permissions.Read.List","ACL.Permissions.Read.Get"]}|
| {"code":400,"message":"Invalid JSON"}                        |                     
| {"code":400,"message":"Error"}                               |

**CREATE GROUP:**
curl -X POST http://localhost:8081/acl/groups/create -H "Content-Type: application/json" -d "{""name"":""name"",""description"":""123456789"",""permissions"":[]}"

| Answers                                                      |
---------------------------------------------------------------|
| {"code":200,"message":"Success"}                             |
| {"code":400,"message":"Invalid JSON"}                        |
| {"code":400,"message":"Error"}                               |
| {"code":400,"message":"The group already exists"}            |
| {"code":200,"message":"No permissions content was found"}    |

**INFORMATION ABOUT GROUP UPDATING:**
curl -X POST http://localhost:8081/acl/groups/update -H "Content-Type: application/json" -d "{""name"":""mailers"",""description"":""1234567810"",""permissions"":[""ACL.Permissions.Read.Check"", ""ACL.Permissions.Read.ListPermissions""]}"

| Answers                                                      |
---------------------------------------------------------------|
| {"code":200,"message":"Success"}                             |
| {"code":400,"message":"Invalid JSON"}                        |
| {"code":400,"message":"Error"}                               |
| {"code":404,"message":"This group not exists"}               |
| {"code":200,"message":"No permissions content was found"}    |

**DELETE GROUP:**
curl -X POST http://localhost:8081/acl/groups/delete -H "Content-Type: application/json" -d "{""idDelete"":""5720a6bf56d06b2bbf907230"",""idRelocate"":""57208e5056d06b2bbf907196""}"

| Answers                                                      |
---------------------------------------------------------------|
| {"code":200,"message":"Success"}                             |
| {"code":400,"message":"Invalid JSON"}                        |
| {"code":400,"message":"Error"}                               |
| {"code":404,"message":"This id doesn't match any document"}  |

**FIND GROUP BY ID:**
curl -X POST http://localhost:8081/acl/groups/get -H "Content-Type: application/json" -d "{""id"":""571a42ee11ad42174d4cf94a""}"

| Answers                                                      |
---------------------------------------------------------------|
| {"code":200,"message":{"id":"571a42ee11ad42174d4cf94a", "name":"name", "description":"123456789", "permissions": ["ACL.Permissions.Read.ListPermissions","ACL.Permissions.Read.Check"]}}|
| {"code":400,"message":"Invalid JSON"}                        |
| {"code":400,"message":"Error"}                               |
| {"code":404,"message":"This id doesn't match any document"}  |

**LIST OF GROUPS:**
curl -X POST http://localhost:8081/acl/groups/list -H "Content-Type: application/json" -d "{}"

| Answers                                                      |
---------------------------------------------------------------|
| {"code":200,"message":{"groups":[{ "id":"571a43b311ad42174d4cf94b", "name":"admins", "description":"123456789", "permissions":["ACL.Permissions.Read.ListPermissions"]}, {"id":"571a43c711ad42174d4cf94c", "name":"users", "description":"123456789", "permissions":[]}, {"id":"571a42ee11ad42174d4cf94a", "name":"name", "description":"123456789", "permissions":["ACL.Permissions.Read.ListPermissions","ACL.Permissions.Read.Check"]}]}}| 
| {"code":400,"message":"Invalid JSON"}                        |
| {"code":400,"message":"Error"}                               |

**PERMISSIONS OF GROUPS:**
curl -X POST http://localhost:8081/acl/groups/permissions -H "Content-Type: application/json" -d "{}"

| Answers                                                      |
|--------------------------------------------------------------
| {"provide": ["ACL.Groups.Read.List", "ACL.Groups.Read.Get", "ACL.Groups.Write.Create", "ACL.Groups.Write.Update"], "required": ["ACL.Permissions.Read.List", "ACL.Permissions.Read.Get"]}|
| {"code":400,"message":"Invalid JSON"}                        |                     
| {"code":400,"message":"Error"}                               |

**LIST OF ALL PERMISSIONS:**
curl -X POST http://localhost:8081/acl/permissions/list -H "Content-Type: application/json" -d "{}"

| Answers                                                      |
---------------------------------------------------------------|
| {"code":200,"message":[{"service":"ACL","permissions":["ACL.Permissions.Read.GetPermissionsByService","ACL.Permissions.Read.ListPermissions"]}]}| 
| {"code":400,"message":"Invalid JSON"}                        |
| {"code":400,"message":"Error"}                               |
| {"code":204,"message":"No Content"}                          |

**LIST OF PERMISSIONS BY SERVICE:**
curl -X POST http://localhost:8081/acl/permissions/list -H "Content-Type: application/json" -d "{""service"":""ACL""}"

| Answers                                                      |
---------------------------------------------------------------|
| {"code":200,"message":[{"service":"ACL","permissions":["ACL.Permissions.Read.GetPermissionsByService","ACL.Permissions.Read.ListPermissions"]}]}| 
| {"code":400,"message":"Invalid JSON"}                        |
| {"code":400,"message":"Error"}                               |
| {"code":204,"message":"No Content"}                          |

**DELETE PERMISSIONS:**
curl -X POST http://localhost:8081/acl/permissions/delete -H "Content-Type: application/json" -d "{""service"":""ACL""}"

| Answers                                                         |
------------------------------------------------------------------|
| {"code":200,"message":"Success"}                                |
| {"code":400,"message":"Invalid JSON"}                           |
| {"code":400,"message":"Error"}                                  |
| {"code":404,"message":"This service doesn't match any account"} |

**IMPORT PERMISSIONS:**
curl -X POST http://localhost:8081/acl/permissions/import -H "Content-Type: application/json" -d "{""service"":""ACL"",""permissions"":[""ACL.Permissions.Read.GetPermissionsByService"",""ACL.Permissions.Read.ListPermissions"",""ACL.Permissions.Read.Check""]}"

| Answers                                                         |
------------------------------------------------------------------|
| {"code":200,"message":"Success"}                                |
| {"code":400,"message":"Invalid JSON"}                           |
| {"code":400,"message":"Error"}                                  |
| {"code":404,"message":"This service already exist"}             |

**CHECK PERMISSIONS:**
curl -X POST http://localhost:8081/acl/permissions/check -H "Content-Type: application/json" -d "{""permissions"":[""ACL.Permissions.Read.GetPermissionsByService"",""ACL.Permissions.Read.ListPermissions"",""ACL.Permissions.Read.Check""]}"

| Answers                                                         |
------------------------------------------------------------------|
| {"code":200,"message":"Success"}                                |
| {"code":206,"message":"Partial Content [\"ACL.Permissions.Read.GetPermissionsByService\", \"ACL.Permissions.Read.ListPermissions\", \"ACL.Permissions.Read.Check\"]"}                                                                  |
| {"code":400,"message":"Invalid JSON"}                           |
| {"code":400,"message":"Error"}                                  |