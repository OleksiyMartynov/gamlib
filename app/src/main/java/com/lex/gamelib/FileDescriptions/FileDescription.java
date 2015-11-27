package com.lex.gamelib.FileDescriptions;

/**
 * Created by Oleksiy on 11/26/2015.
 */
public abstract class FileDescription {

    public abstract String getFileName();

    public abstract DescriptionProvider.FileDescriptionTypes getType();

    public abstract boolean equals(DescriptionProvider.FileDescriptionTypes type);
}
