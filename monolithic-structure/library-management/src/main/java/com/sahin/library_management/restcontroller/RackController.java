package com.sahin.library_management.restcontroller;

import com.sahin.library_management.infra.entity.RackEntity;
import com.sahin.library_management.service.RackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/racks")
public class RackController {

    @Autowired
    private RackService rackService;

    @PostMapping("create")
    public ResponseEntity<RackEntity> createRack(@RequestBody RackEntity rack) {
        RackEntity createdRack = rackService.createRack(rack);
        return ResponseEntity.ok(createdRack);
    }

    @PutMapping("update")
    public ResponseEntity<RackEntity> updateRack(@RequestBody RackEntity rack) {
        RackEntity updatedRack = rackService.updateRack(rack);
        return ResponseEntity.ok(updatedRack);
    }

    @DeleteMapping("delete/{rackId}")
    public ResponseEntity<Void> deleteRackById(@PathVariable Long rackId) {
        rackService.deleteRackById(rackId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("get/{rackId}")
    public ResponseEntity<RackEntity> getRackById(@PathVariable Long rackId) {
        return ResponseEntity.ok(rackService.getRackById(rackId));
    }

    @GetMapping("getAll")
    public ResponseEntity<List<RackEntity>> getAll() {
        return ResponseEntity.ok(rackService.getAll());
    }
}
