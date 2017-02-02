package com.move4mobile.apps.bite.objects;

import java.util.HashSet;

/**
 * Created by casvd on 2-2-2017.
 */
public class ArchiveProductTotal {

    private ArchiveProduct product;
    private HashSet<User> users;

    public ArchiveProductTotal(ArchiveProduct product, HashSet<User> users) {
        this.product = product;
        this.users = users;
    }

    public ArchiveProduct getProduct() {
        return product;
    }

    public void updateProduct(ArchiveProduct product) {
        this.product = new ArchiveProduct(this.product.getAmount() + product.getAmount(), this.product.getName(), this.product.getPrice());
    }

    public void addUser(User user) {
        users.add(user);
    }

    public HashSet<User> getUsers() {
        return users;
    }
}
