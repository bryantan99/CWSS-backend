package com.chis.communityhealthis.utility;

import java.util.ArrayList;
import java.util.List;

public class ListComparator<T> {

    private List<T> oriList;
    private List<T> newList;
    private List<T> listToAdd;
    private List<T> listToDelete;
    private List<T> listToUpdate;

    public ListComparator(List<T> oriList, List<T> newList) {
        this.oriList = oriList;
        this.newList = newList;
        this.listToAdd = differenceBetweenTwoList(newList, oriList);
        this.listToDelete = differenceBetweenTwoList(oriList, newList);
    }

    public List<T> getOriList() {
        return oriList;
    }

    public void setOriList(List<T> oriList) {
        this.oriList = oriList;
    }

    public List<T> getNewList() {
        return newList;
    }

    public void setNewList(List<T> newList) {
        this.newList = newList;
    }

    public List<T> getListToAdd() {
        return listToAdd;
    }

    public void setListToAdd(List<T> listToAdd) {
        this.listToAdd = listToAdd;
    }

    public List<T> getListToDelete() {
        return listToDelete;
    }

    public void setListToDelete(List<T> listToDelete) {
        this.listToDelete = listToDelete;
    }

    public List<T> getListToUpdate() {
        return listToUpdate;
    }

    public void setListToUpdate(List<T> listToUpdate) {
        this.listToUpdate = listToUpdate;
    }

    private List<T> differenceBetweenTwoList(List<T> list1, List<T> list2) {
        List<T> differences = new ArrayList<>(list1);
        differences.removeAll(list2);
        return differences;
    }
}
