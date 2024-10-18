package com.foogaro.controller;

import com.foogaro.model.Beer;
import com.foogaro.service.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/beers")
public class BeerController {

    private final List<Beer> beers = new ArrayList<>();

    @Autowired
    private BeerService beerService;

    @PostMapping
    public ResponseEntity<Beer> createBeer(@RequestBody Beer beer) {
        beerService.createBeer(beer);
        return ResponseEntity.status(HttpStatus.CREATED).body(beer);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Beer> readBeer(@PathVariable String name) {
        Beer beer =  beerService.readBeer(name);
        return beer != null ? ResponseEntity.ok(beer) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{name}")
    public ResponseEntity<Beer> updateBeer(@PathVariable String name, @RequestBody Beer updatedBeer) {
        beerService.updateBeer(name, updatedBeer);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteBeer(@PathVariable String name) {
        beerService.deleteBeer(name);
        return ResponseEntity.noContent().build();
    }
}