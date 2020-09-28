package ru.otus.sc.greet.service.impl

import ru.otus.sc.greet.dao.GreetingDao
import ru.otus.sc.greet.model.{GreetRequest, GreetResponse}
import ru.otus.sc.greet.service.GreetingService

class GreetingServiceImpl(dao: GreetingDao) extends GreetingService {
  def greet(request: GreetRequest): GreetResponse =
    if (request.isHuman)
      if (request.age >= 18)
        GreetResponse(s"${dao.greetingPrefix} ${request.name} ${dao.greetingPostfix} You are adult")
      else
        GreetResponse(
          s"${dao.greetingPrefix} ${request.name} ${dao.greetingPostfix} You are minor"
        )
    else GreetResponse("AAAAAAAAAA!!!!!!")
}
