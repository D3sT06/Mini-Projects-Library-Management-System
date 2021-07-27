package com.sahin.library_management.restcontroller;

import com.sahin.library_management.infra.annotation.LogExecutionTime;
import com.sahin.library_management.infra.model.book.Rack;
import com.sahin.library_management.infra.validator.RackValidator;
import com.sahin.library_management.service.RackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/racks")
@LogExecutionTime
public class RackController {

    @Autowired
    private RackService rackService;

    @Autowired
    private RackValidator rackValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(rackValidator);
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PostMapping("create")
    public ResponseEntity<Rack> createRack(@RequestBody @Valid Rack rack) {
        Rack createdRack = rackService.createRack(rack);
        return ResponseEntity.ok(createdRack);
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @PutMapping("update")
    public ResponseEntity<Rack> updateRack(@RequestBody @Valid Rack rack) {
        Rack updatedRack = rackService.updateRack(rack);
        return ResponseEntity.ok(updatedRack);
    }

    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @DeleteMapping("delete/{rackId}")
    public ResponseEntity<Void> deleteRackById(@PathVariable Long rackId) {
        rackService.deleteRackById(rackId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_LIBRARIAN')")
    @GetMapping("get/{rackId}")
    public ResponseEntity<Rack> getRackById(@PathVariable Long rackId) {
        return ResponseEntity.ok(rackService.getRackById(rackId));
    }


    @PreAuthorize("hasRole('ROLE_LIBRARIAN')")
    @GetMapping("getAll")
    public ResponseEntity<List<Rack>> getAll() {
        return ResponseEntity.ok(rackService.getAll());
    }
}
