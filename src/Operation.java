class Operation {
    String ope;
    int priority;

    public Operation() {
    }

    public Operation(String ope, int priority) {
        this.ope = ope;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return ope;
    }
}
