package com.alek.influentialpeople.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alek.influentialpeople.persistence.entity.Hero;
import com.alek.influentialpeople.service.HeroService;

@RestController
public class HeroController { // potrrzebuje jsona 
	
	@Autowired
	HeroService personService;
	
	@RequestMapping(path = "/hero", method = RequestMethod.GET)
	public List<Hero> getAllPersons() {
		
		return personService.getAllPersons();
	}

	@RequestMapping(path = "/hero", method = RequestMethod.POST)
	public void addPerson(@RequestBody Hero person) {
		System.out.println(person.toString()+"  !!!!!!!!!!!!!!!!!!!!!");
		System.out.println();
		personService.addPerson(person);
	}

//	@RequestMapping(path = "/user", method = RequestMethod.GET)
//	public User getUser3() {
//
//		return new User();
//	}
//
//	@RequestMapping(path = "/user", method = RequestMethod.GET)
//	public User getUser4() {
//
//		return new User();
//	}
//
//	@RequestMapping(path = "/user", method = RequestMethod.GET)
//	public User getUser5() {
//
//		return new User();
//	}
//
//	@RequestMapping(path = "/user", method = RequestMethod.GET)
//	public User getUs1er() {
//
//		return new User();
//	}

}