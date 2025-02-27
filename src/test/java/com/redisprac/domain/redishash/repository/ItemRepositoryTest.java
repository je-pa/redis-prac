package com.redisprac.domain.redishash.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.redisprac.domain.redishash.model.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ItemRepositoryTest {
  @Autowired
  private ItemRepository itemRepository;

  @Test
  public void createTest() {
    Item item = Item.builder()
        .id(1L)
        .name("keyboard")
        .description("Mechanical Keyboard")
        .build();
    itemRepository.save(item);
  }

  @Test
  public void readOneTest() {
    Item item = itemRepository.findById(1L)
        .orElseThrow();
    assertThat(item.getDescription()).isEqualTo("Mechanical Keyboard");
  }


  @Test
  public void updateTest() {
    Item item = itemRepository.findById(1L)
        .orElseThrow();
    item.setDescription("On Sale!!!");
    itemRepository.save(item);

    item = itemRepository.findById(1L)
        .orElseThrow();
    assertThat(item.getDescription()).isEqualTo("On Sale!!!");
  }

  @Test
  public void deleteTest() {
    itemRepository.deleteById(1L);
  }
}