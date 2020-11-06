package com.sahin.library_management.controller.swagger;

import com.sahin.library_management.infra.model.book.Rack;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(tags = "Racks")
public interface RackSwaggerApi {

    @ApiOperation(value = "Create a rack",
            response = Rack.class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<Rack> createRack(
            @ApiParam("Rack object") Rack rack);

    @ApiOperation(value = "Update the rack",
            response = Rack.class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<Rack> updateRack(
            @ApiParam("Rack object") Rack rack);

    @ApiOperation(value = "Delete the rack by id",
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<Void> deleteRackById(
            @ApiParam("Id of the rack") Long rackId);

    @ApiOperation(value = "Get the rack by id",
            response = Rack.class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<Rack> getRackById(
            @ApiParam("Id of the rack") Long rackId);

    @ApiOperation(value = "Get all racks",
            response = Rack[].class,
            authorizations = @Authorization(value = "basicAuth"))
    ResponseEntity<List<Rack>> getAll();
}
