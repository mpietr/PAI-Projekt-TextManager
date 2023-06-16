package put.poznan.textmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import put.poznan.textmanager.FilterRequest;
import put.poznan.textmanager.exception.ResourceNotFoundException;
import put.poznan.textmanager.model.TextResource;
import put.poznan.textmanager.model.User;
import put.poznan.textmanager.repository.TextResourceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class TextResourceService {
    private final TextResourceRepository textResourceRepository;

    @Autowired
    public TextResourceService(TextResourceRepository textResourceRepository) {
        this.textResourceRepository = textResourceRepository;
    }

    public TextResource addTextResource(TextResource textResource) {
        textResource.setCode(UUID.randomUUID().toString());
        return textResourceRepository.save(textResource);
    }

    public TextResource findTextResourceByNameAndOwnerId(String name, User user) {
        return textResourceRepository.findTextResourceByNameAndOwnerId(name, user)
                .orElseThrow(() -> new ResourceNotFoundException("TextResource by code " + name + " not found"));
    }

    public TextResource findTextResourceByCode(String code) {
        return textResourceRepository.findTextResourceByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("TextResource by code " + code + " not found"));
    }

    public List<TextResource> findAllTextResources() {
        return textResourceRepository.findAll();
    }

    public List<TextResource> findAllTextResourcesByOwnerId(User user) {
        return textResourceRepository.findAllByOwnerId(user);
    }

    public TextResource updateTextResource(TextResource textResource) {
        return textResourceRepository.save(textResource);
    }

    public void deleteTextResource(String code) {
        textResourceRepository.deleteTextResourceByCode(code);
    }

    public List<TextResource> findFilteredTextResources(FilterRequest filterRequest) {
        String name = filterRequest.name().strip();
        String text = filterRequest.text().strip();
        String tags = filterRequest.tags().strip();
        User user = new User();
        user.setUserId(filterRequest.id());

        boolean nameEmpty = name.isEmpty();
        boolean textEmpty = text.isEmpty();
        boolean tagsEmpty = tags.isEmpty();
        if(!nameEmpty && !textEmpty && !tagsEmpty) {
            //everything
            List<TextResource> result = textResourceRepository.findAllByNameContainingAndTextContainingAndOwnerId(name, text, user);
            result.retainAll(filterTags(textResourceRepository, tags, user));
            return result;
        }
        if(!nameEmpty && !textEmpty && tagsEmpty) {
            //name and text
            return textResourceRepository.findAllByNameContainingAndTextContainingAndOwnerId(name, text, user);
        }
        if(!nameEmpty && textEmpty && !tagsEmpty) {
            //name and tags
            List<TextResource> result = textResourceRepository.findAllByNameContainingAndOwnerId(name, user);
            result.retainAll(filterTags(textResourceRepository, tags, user));
            return result;
        }
        if(nameEmpty && !textEmpty && !tagsEmpty) {
            //text and tags
            List<TextResource> result = textResourceRepository.findAllByTextContainingAndOwnerId(name, user);
            result.retainAll(filterTags(textResourceRepository, tags, user));
            return result;
        }
        if(!nameEmpty && textEmpty && tagsEmpty) {
            //name
            return textResourceRepository.findAllByNameContainingAndOwnerId(name, user);
        }
        if(nameEmpty && !textEmpty && tagsEmpty) {
            //text
            return textResourceRepository.findAllByTextContainingAndOwnerId(text, user);
        }
        if(nameEmpty && textEmpty && !tagsEmpty) {
            //tags
            return filterTags(textResourceRepository, tags, user);
        }

        return textResourceRepository.findAllByOwnerId(user);
    }

    private List<TextResource> filterTags(TextResourceRepository textResourceRepository, String tags, User user) {
        List<String> tagList = List.of(tags.split(" "));
        List<TextResource> result = textResourceRepository.findAllByTagsContainingAndOwnerId(tagList.get(0), user);
        if (tagList.size() < 2) {
            return result;
        }
        for(int i = 1; i < tagList.size(); i++) {
            result.retainAll(textResourceRepository.findAllByTagsContainingAndOwnerId(tagList.get(i), user));
        }
        return result;
    }
}
