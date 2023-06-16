package put.poznan.textmanager.controller;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Text;
import put.poznan.textmanager.FilterRequest;
import put.poznan.textmanager.exception.ResourceNotFoundException;
import put.poznan.textmanager.exception.UserNotFoundException;
import put.poznan.textmanager.model.TextResource;
import put.poznan.textmanager.model.User;
import put.poznan.textmanager.service.TextResourceService;
import put.poznan.textmanager.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/text")
public class TextResourceController {
    private final TextResourceService textResourceService;
    private final UserService userService;

    Logger logger = LoggerFactory.getLogger("SampleLogger");


    public TextResourceController(TextResourceService textResourceService, UserService userService) {
        this.textResourceService = textResourceService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<TextResource> addTextResource(@RequestBody TextResource newTextResource) {
        try {
            TextResource tr = textResourceService.findTextResourceByNameAndOwnerId(newTextResource.getName(), newTextResource.getOwnerId());
        } catch (ResourceNotFoundException e) {
            TextResource textResource = textResourceService.addTextResource(newTextResource);
            return new ResponseEntity<>(textResource, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/update")
    public ResponseEntity<TextResource> updateTextResource(@RequestBody TextResource editedTextResource) {
        try {
            TextResource tr = textResourceService.findTextResourceByCode(editedTextResource.getCode());
            try {
                TextResource tr2 = textResourceService.findTextResourceByNameAndOwnerId(editedTextResource.getName(), editedTextResource.getOwnerId());
                if (!Objects.equals(tr.getCode(), tr2.getCode())) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            } catch (Exception ignored) {}

            tr.setText(editedTextResource.getText());
            tr.setName(editedTextResource.getName());
            tr.setTags(editedTextResource.getTags());
            TextResource textResource = textResourceService.updateTextResource(tr);
            return new ResponseEntity<>(textResource, HttpStatus.CREATED);

        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/filter")
    public ResponseEntity<List<TextResource>> filterTextResource(@RequestBody FilterRequest filterRequest) {
        List<TextResource> result = textResourceService.findFilteredTextResources(filterRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/delete/{code}")
    @Transactional
    public ResponseEntity<?> deleteEmployee(@PathVariable("code") String code) {
        textResourceService.deleteTextResource(code);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/find/{id}")
    public ResponseEntity<List<TextResource>> findAllTextResourcesByUserId(@PathVariable("id") Long userId) {
        User user = null;
        logger.info(userId.toString());
        try {
            user = userService.findUserById(userId);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<TextResource> textResources = textResourceService.findAllTextResourcesByOwnerId(user);

        return new ResponseEntity<>(textResources, HttpStatus.OK);

    }

    @GetMapping("/details/{code}")
    public ResponseEntity<TextResource> findTextResourceByCode(@PathVariable("code") String code){
        TextResource textResource = null;
        try {
            textResource = textResourceService.findTextResourceByCode(code);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(textResource, HttpStatus.OK);
    }



}
