package com.example.batchsample.config;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.batchsample.entity.User;

//@Component
public class UserItemProcessor implements ItemProcessor<User, User> {
	
	@Override
	public User process(final User user) throws Exception {
		System.out.println("Item Processor  start");
//		final String firstName = user.getFirstName().toUpperCase();
//		final String lastName = user.getLastName().toUpperCase();
//
//		final User transformedPerson = new User(firstName, lastName);
//		System.out.println("Item Processor end");
//		return transformedPerson;
		return user;
	}

}
