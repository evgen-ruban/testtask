package org

import spray.http.StatusCode

package object accounts {
  implicit def Status2Code(status: StatusCode): Int = status.intValue
}
