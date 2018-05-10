package com.meetup.meetup.rest.controller;



import com.meetup.meetup.entity.Item;

import com.meetup.meetup.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.meetup.meetup.service.ItemService;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/item")
public class ItemController {

    private static Logger log = LoggerFactory.getLogger(ItemController.class);

    private final ItemService itemService;
    private final StorageService storageService;

    @Autowired
    public ItemController(ItemService itemService, StorageService storageService) {
        this.itemService = itemService;
        this.storageService = storageService;
    }


    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<Item> getItemById(@PathVariable int id){
        log.debug("Try to get item with id '{}'", id);
        Item item = itemService.getItemById(id);

        log.debug("Send response body item '{}' and status OK", item);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @PostMapping
    public @ResponseBody ResponseEntity<Item> addItem(@Valid @RequestBody Item item) {
        log.debug("Trying to save item {}", item);
        Item addedItem = itemService.addItem(item);

        log.debug("Send response body saved item '{}' and status CREATED", addedItem);
        return new ResponseEntity<>(addedItem, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/add")
    public @ResponseBody ResponseEntity<Item> addItemToUserWishList(@Valid @RequestBody Item item, @PathVariable("id") String id){
        log.debug("Trying to add item with id '{}' to user wish list", item.getItemId());
        Item returnedItem = itemService.addItemToUserWishList(item);
        log.info("Added item with id '{}' to user wish list", returnedItem.getItemId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    public @ResponseBody ResponseEntity deleteItem(@PathVariable int id) {
        log.debug("Trying to delete item with id '{}' to user wish list", id);
        Item deletedItem = itemService.deleteItemFromUserWishList(id);

        log.debug("Send response status OK");
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping
    public @ResponseBody ResponseEntity<Item> updateItem(@Valid @RequestBody Item newItem) {
        log.debug("Trying to update item '{}'", newItem);
        Item updatedItem = itemService.updateItem(newItem);

       log.debug("Send response body updated '{}' and status OK");
        return new ResponseEntity<>(updatedItem, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public @ResponseBody ResponseEntity deleteItem(@Valid @RequestBody Item item) {
        log.debug("Trying to delete item '{}'", item);
        Item deletedItem = itemService.deleteItem(item);

        log.debug("Send response status OK");
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam MultipartFile file) {
        log.debug("Trying to upload image '{}'", file);

        String imagePath = storageService.wishItemImageStore(file);

        log.debug("Image successfully uploaded send response status OK");
        return new ResponseEntity<>(imagePath, HttpStatus.OK);
    }

    //Booking

    @PostMapping("/{itemId}/owner/{ownerId}/booker/{bookerId}")
    public ResponseEntity<Item> addItemBooker(@PathVariable int itemId, @PathVariable int ownerId, @PathVariable int bookerId){
        log.debug("Trying to add item with id '{}' to user wish list", itemId);

        Item itemWithBooker = itemService.addItemBooker(ownerId, itemId, bookerId);

        log.debug("Booker with id '{}' was added to item '{}'", bookerId, itemWithBooker);

        return new ResponseEntity<>(itemWithBooker, HttpStatus.OK);
    }

    @DeleteMapping("/{itemId}/owner/{ownerId}/booker/{bookerId}")
    public ResponseEntity deleteItemBooker(@PathVariable int itemId, @PathVariable int ownerId, @PathVariable int bookerId) {
        log.debug("Trying to delete item with id '{}' to user wish list", itemId);

        Item itemWithoutBooker = itemService.deleteItemBooker(ownerId, itemId);

        log.debug("Booker with id '{}' was deleted from item '{}'", bookerId, itemWithoutBooker);
        return new ResponseEntity<>(itemWithoutBooker, HttpStatus.OK);
    }
}
