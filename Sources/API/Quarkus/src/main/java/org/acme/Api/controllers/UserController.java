package org.acme.api.controllers;

import java.net.URI;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.acme.api.BowlDbContext;
import org.acme.api.dto.UserDTO;
import org.acme.hibernates.entities.UserEntity;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.logging.Logger;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/users")
public class UserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    @Inject
    BowlDbContext service;

    @GET
    @Operation(summary = "Get all users")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<UserDTO>> getUsers() {
        LOGGER.info("Get all users");
        return service.userRepository.findAll().project(UserDTO.class).list();
    }

    @GET
    @Operation(summary = "Get a user by ID")
    @APIResponse(responseCode = "200", description = "OK")
    @APIResponse(responseCode = "404", description = "User not found")
    @Path("/{id}")
    public Uni<Response> getUserById(@PathParam("id") Long id) {
        LOGGER.info("Get user with id : " + id);
        return service.userRepository.getUserById(id)
                .map(user -> user == null ? Response.status(Status.NOT_FOUND) : Response.ok(user).status(200))
                .map(Response.ResponseBuilder::build);
    }

    @GET
    @Operation(summary = "Get a user by name")
    @APIResponse(responseCode = "200", description = "OK")
    @APIResponse(responseCode = "404", description = "User not found")
    @Path("/name/{name}")
    public Uni<Response> getUserByName(@PathParam("name") String name) {
        LOGGER.info("Get user with name : " + name);
        return service.userRepository.findwithName(name)
                .map(user -> user == null ? Response.status(Status.NOT_FOUND) : Response.ok(user).status(200))
                .map(Response.ResponseBuilder::build);
    }

    @GET
    @Operation(summary = "Get a user by mail")
    @APIResponse(responseCode = "200", description = "OK")
    @APIResponse(responseCode = "404", description = "User not found")
    @Path("/mail/{mail}")
    public Uni<Response> getUserByMail(@PathParam("mail") String mail) {
        LOGGER.info("Get user with mail : " + mail);
        return service.userRepository.findwithMail(mail)
                .map(user -> user == null ? Response.status(Status.NOT_FOUND) : Response.ok(user).status(200))
                .map(Response.ResponseBuilder::build);
    }

    @POST
    @Operation(summary = "Create a new user")
    @APIResponse(responseCode = "201", description = "User successfully created")
    @APIResponse(responseCode = "422", description = "User invalidly set on request")
    @ReactiveTransactional
    public Uni<Response> createUser(UserEntity user) {
        if (user == null) {
            throw new WebApplicationException("user was invalidly set on request.", 422);
        }
        LOGGER.info("creating user: " + user.getName());
        return service.userRepository.persist(user)
                .map(persistedUser -> Response
                        .created(URI.create("/users/" + user.id))
                        .entity(persistedUser)
                        .build())
                .onFailure().recoverWithItem(Response.status(Status.BAD_REQUEST).build());
    }

    @PUT
    @Operation(summary = "Update a user")
    @APIResponse(responseCode = "200", description = "OK")
    @APIResponse(responseCode = "404", description = "User not found")
    @Path("/{id}")
    @ReactiveTransactional
    public Uni<Response> updateUser(@PathParam("id") Long id, UserEntity newUser) {
        LOGGER.info("Update user with id : " + id);
        return service.userRepository.findById(id)
                .onItem().ifNull().failWith(() -> new WebApplicationException("User not found", Status.NOT_FOUND))
                .onItem().ifNotNull().invoke(oldUser -> {
                    oldUser.setName(newUser.getName());
                })
                .onItem().ifNotNull().transform(entity -> Response.ok(entity).build());
    }

    @DELETE
    @Operation(summary = "Delete a user")
    @APIResponse(responseCode = "204")
    @APIResponse(responseCode = "404", description = "User not found")
    @Path("/{id}")
    @ReactiveTransactional
    public Uni<Response> delete(@PathParam("id") Long id) {
        LOGGER.info("Delete user with id : " + id);
        return service.userRepository.deleteById(id)
                .onItem().transform(entity -> !entity ? Response.status(Status.NOT_FOUND).build()
                        : Response.noContent().build());
    }

    @GET
    @Operation(summary = "Get the number of users")
    @Path("/count")
    public Uni<Long> count() {
        LOGGER.info("Get user count");
        return service.userRepository.count();
    }
}