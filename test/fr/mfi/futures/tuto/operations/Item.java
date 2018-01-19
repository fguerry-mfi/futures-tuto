/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.mfi.futures.tuto.operations;

/**
 *
 * @author fguerry
 */
public class Item {

    public int id;
    public String name;

    public Item() {
    }

    public Item(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", name=" + name + '}';
    }

}
