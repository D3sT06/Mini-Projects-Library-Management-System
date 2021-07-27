package com.sahin.library_management.restcontroller;

import com.sahin.library_management.infra.model.book.Rack;
import com.sahin.library_management.service.RackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/racks")
public class RackController {

    @Autowired
    private RackService rackService;

    @PostMapping("create")
    public ResponseEntity<Rack> createRack(@RequestBody @Valid Rack rack) {
        Rack createdRack = rackService.createRack(rack);
        return ResponseEntity.ok(createdRack);
    }

    @PutMapping("update")
    public ResponseEntity<Rack> updateRack(@RequestBody @Valid Rack rack) {
        Rack updatedRack = rackService.updateRack(rack);
        return ResponseEntity.ok(updatedRack);
    }

    @DeleteMapping("delete/{rackId}")
    public ResponseEntity<Void> deleteRackById(@PathVariable Long rackId) {
        rackService.deleteRackById(rackId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("get/{rackId}")
    public ResponseEntity<Rack> getRackById(@PathVariable Long rackId) {
        return ResponseEntity.ok(rackService.getRackById(rackId));
    }

    @GetMapping("getAll")
    public ResponseEntity<List<Rack>> getAll() {
        return ResponseEntity.ok(rackService.getAll());
    }
}
