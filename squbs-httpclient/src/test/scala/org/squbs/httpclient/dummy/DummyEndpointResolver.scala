/*
 *  Copyright 2015 PayPal
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.squbs.httpclient.dummy

import akka.actor.ActorSystem
import org.squbs.httpclient.endpoint.{Endpoint, EndpointResolver}
import org.squbs.httpclient.env.{DEV, Default, Environment}
import org.squbs.httpclient.HttpClientException
import DummyService._

class DummyServiceEndpointResolver(implicit system: ActorSystem) extends EndpointResolver{

  override def resolve(svcName: String, env: Environment): Option[Endpoint] = {
    if (svcName == name) {
      Some(Endpoint(dummyServiceEndpoint))
    } else {
      None
    }
  }

  override def name: String = "DummyService"
}

class NotExistingEndpointResolver(implicit system: ActorSystem) extends EndpointResolver {

  override def resolve(svcName: String, env: Environment): Option[Endpoint] = {
    if (svcName == name) {
      Some(Endpoint("http://www.notexistingservice.com"))
    } else {
      None
    }
  }

  override def name: String = "NotExistingService"
}

class DummyLocalhostResolver(implicit system: ActorSystem) extends EndpointResolver {
  override def resolve(svcName: String, env: Environment = Default): Option[Endpoint] = {
    require(svcName != null, "Service name cannot be null")
    require(svcName.length > 0, "Service name must not be blank")

    env match {
      case Default | DEV => Some(Endpoint("http://localhost:8080"))
      case _   => throw new HttpClientException("DummyLocalhostResolver cannot support " + env + " environment")
    }
  }

  override def name: String = "DummyLocalhostResolver"
}

object GoogleAPI {

  class GoogleMapAPIEndpointResolver(implicit system: ActorSystem) extends EndpointResolver {
    override def resolve(svcName: String, env: Environment = Default): Option[Endpoint] = {
      if (svcName == name)
        Some(Endpoint("http://maps.googleapis.com/maps"))
      else
        None
    }

    override def name: String = "googlemap"
  }

  case class Elevation(location: Location, elevation: Double)
  case class Location(lat: Double, lng: Double)
  case class GoogleApiResult[T](status: String, results: List[T])

}

