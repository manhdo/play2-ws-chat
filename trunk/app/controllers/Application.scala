package controllers

import play.api._
import play.api.mvc._

import play.api.libs.json._
import play.api.libs.iteratee._

import models._

import akka.actor._
import akka.util.duration._

object Application extends Controller {
  
  def index = Action { implicit request =>
    Ok(views.html.index("Your new application is ready."))
  }
  
  def chatRoom(username: Option[String], roomName: Option[String]) = Action {
    implicit request =>
      username.filterNot(_.isEmpty).map { username =>
      Ok(views.html.chatRoom(username)(roomName.getOrElse("default")))
    }.getOrElse {
      Redirect(routes.Application.index).flashing(
        "error" -> "Please choose a valid username."
      )
    }
  }
  
  def chat(username: String, roomName: String) = WebSocket.async[JsValue] {
    request => ChatRoom.join(username, roomName)
  }
  
}