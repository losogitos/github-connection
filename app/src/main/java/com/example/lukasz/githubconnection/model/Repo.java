package com.example.lukasz.githubconnection.model;

import com.google.common.base.Objects;

/**
 * Created by Łukasz Ciupa on 28.04.17.
 */

public final class Repo {

    long id;
    String name;
    String description;

    public Repo() {
    }

    public Repo(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getName(){
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Repo repo = (Repo) o;
        return Objects.equal(id, repo.id) &&
                Objects.equal(name, repo.name) &&
                Objects.equal(description, repo.description);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, description);
    }

}
