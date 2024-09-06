package com.jetbrains.fileindexing.service;

import com.jetbrains.fileindexing.utils.Status;

import java.util.Objects;

public class IndexingStatusServiceImpl implements IndexingStatusService {

    private Status status = Status.FAILED;

    @Override
    public void start() {
        setStatus(Status.INDEXING);
    }

    @Override
    public void success() {
        setStatus(Status.READY);
    }

    @Override
    public void fail() {
        setStatus(Status.FAILED);
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public boolean statusIs(Status status) {
        return Objects.equals(getStatus(), status);
    }

    private void setStatus(Status status) {
        this.status = status;
    }

}
