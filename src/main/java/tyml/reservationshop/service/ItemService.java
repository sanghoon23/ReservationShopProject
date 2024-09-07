package tyml.reservationshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tyml.reservationshop.domain.Item;
import tyml.reservationshop.domain.Place;
import tyml.reservationshop.domain.dto.ItemModifyForm;
import tyml.reservationshop.repository.ItemRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public Long join(Item item) {
        itemRepository.save(item);
        return item.getId();
    }

    @Transactional
    public void saveAll(List<Item> items) {
        itemRepository.saveAll(items);
    }

    @Transactional
    public void updateItem(Long itemId, ItemModifyForm itemModifyForm){
        Item findItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item ID: " + itemId));

        findItem.updateItem(itemModifyForm);
    }

    @Transactional
    public void deleteItem(Long itemId) {
        Item findItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item ID: " + itemId));
        itemRepository.delete(findItem);
    }

    public Item findOne(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(()-> new IllegalArgumentException("Invalid item ID:" + itemId));
    }

    public List<Item> findItemsByIds(List<Long> Ids) {
        return itemRepository.findItemsByIds(Ids);
    }

    public List<Item> findByPlaceId(Long placeId) {
        return itemRepository.findByPlaceId(placeId);
    }
}
