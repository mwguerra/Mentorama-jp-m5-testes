package com.mwguerra.jpm5testes.database;

import com.mwguerra.jpm5testes.interfaces.IDatabaseTable;
import com.mwguerra.jpm5testes.interfaces.IEntity;

import java.util.*;

public abstract class DatabaseTable<T extends IEntity> implements IDatabaseTable<T> {
  Map<UUID, T> databaseTable = new HashMap<>();

  public List<T> all() {
    List<T> items = new ArrayList<>();

    for (Map.Entry<UUID, T> item: databaseTable.entrySet()) {
      items.add(item.getValue());
    }

    return items;
  }

  public T create(T item) {
    UUID newId;

    do {
      newId = UUID.randomUUID();
    } while (databaseTable.containsKey(newId));

    item.setId(newId);

    databaseTable.put(newId, item);

    return item;
  }

  public void update(UUID id, T item) {
    databaseTable.put(id, item);
  }

  public T delete(UUID id) {
    return databaseTable.remove(id);
  }

  public T find(UUID id) {
    return databaseTable.get(id);
  }

}
