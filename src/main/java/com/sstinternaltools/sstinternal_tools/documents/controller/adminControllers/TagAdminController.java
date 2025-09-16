package com.sstinternaltools.sstinternal_tools.documents.controller.adminControllers;

import com.sstinternaltools.sstinternal_tools.documents.dto.tagDtos.TagCreateDto;
import com.sstinternaltools.sstinternal_tools.documents.dto.tagDtos.TagResponseDto;
import com.sstinternaltools.sstinternal_tools.documents.dto.tagDtos.TagUpdateDto;
import com.sstinternaltools.sstinternal_tools.documents.service.interfaces.TagService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/document/admin/tag")
public class TagAdminController {

    private final TagService tagService;

    public TagAdminController(TagService tagService) {
        this.tagService = tagService;
    }

    //  Create Tag
    @PostMapping("/create")
    public ResponseEntity<TagResponseDto> createTag(@Valid @RequestBody TagCreateDto tagCreateDto) {
        TagResponseDto createdTag = tagService.createTag(tagCreateDto);
        return ResponseEntity.ok(createdTag);
    }

    //  Update Tag
    @PutMapping("/update/{id}")
    public ResponseEntity<TagResponseDto> updateTag(@PathVariable Long id,
                                                    @Valid @RequestBody TagUpdateDto tagUpdateDto) {
        TagResponseDto updatedTag = tagService.updateTag(id, tagUpdateDto);
        return ResponseEntity.ok(updatedTag);
    }

    //  Delete Tag
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.ok("Tag deleted Successfully");
    }

    // Get All Tags
    @GetMapping("/getAllTags")
    public ResponseEntity<List<TagResponseDto>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    // Get Tag by ID
    @GetMapping("getById/{id}")
    public ResponseEntity<TagResponseDto> getTagById(@PathVariable Long id) {
        return ResponseEntity.ok(tagService.getTagById(id));
    }

    // Search Tags by Name
    @GetMapping("/search")
    public ResponseEntity<List<TagResponseDto>> searchTagsByName(@RequestParam String keyword) {
        return ResponseEntity.ok(tagService.searchTagsByName(keyword));
    }
}