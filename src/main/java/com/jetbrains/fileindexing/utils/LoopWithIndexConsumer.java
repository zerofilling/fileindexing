package com.jetbrains.fileindexing.utils;

import java.util.Collection;

/**
 * A functional interface that represents an operation that accepts an element of type {@code T},
 * its index in a collection.
 *
 * @param <T> the type of the elements in the collection
 */
@FunctionalInterface
public interface LoopWithIndexConsumer<T> {

    /**
     * Performs this operation on the given element, index.
     *
     * @param t the element to process
     * @param index the index of the element in the collection
     */
    void accept(T t, int index);

    /**
     * Iterates over a collection, passing each element, its index
     * to the specified {@code LoopWithIndexConsumer}.
     *
     * @param <T> the type of the elements in the collection
     * @param collection the collection to iterate over
     * @param consumer the {@code LoopWithIndexConsumer} to apply to each element
     */
    static <T> void forEach(Collection<T> collection, LoopWithIndexConsumer<T> consumer) {
        int index = 0;
        for (T t : collection) {
            consumer.accept(t, index++);
        }
    }
}
