package com.sahin.library_management.swagger.controller;

import com.sahin.library_management.infra.model.book.Rack;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(tags = "Racks")
public interface RackSwaggerApi {

    @ApiOperation(value = "Create a rack",
            response = Rack.class,
            authorizations = @Authorization(value = "bearer"))
    @ApiImplicitParams(
            @ApiImplicitParam(
                    name = "rack",
                    value = "Rack object",
                    required = true,
                    dataType = "com.sahin.library_management.swagger.model.RackCreateRequest",
                    paramType = "body"
            )
    )
    ResponseEntity<Rack> createRack(
            @ApiParam(hidden = true) Rack rack);

    @ApiOperation(value = "Update the rack",
            response = Rack.class,
            authorizations = @Authorization(value = "bearer"))
    @ApiImplicitParams(
            @ApiImplicitParam(
                    name = "rack",
                    value = "Rack object",
                    required = true,
                    dataType = "com.sahin.library_management.swagger.model.RackUpdateRequest",
                    paramType = "body"
            )
    )
    ResponseEntity<Rack> updateRack(
            @ApiParam(hidden = true) Rack rack);

    @ApiOperation(value = "Delete the rack by id",
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<Void> deleteRackById(
            @ApiParam("Id of the rack") Long rackId);

    @ApiOperation(value = "Get the rack by id",
            response = Rack.class,
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<Rack> getRackById(
            @ApiParam("Id of the rack") Long rackId);

    @ApiOperation(value = "Get all racks",
            response = Rack[].class,
            authorizations = @Authorization(value = "bearer"))
    ResponseEntity<List<Rack>> getAll();
}
