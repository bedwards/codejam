package name.etapic.gittraining;

public class Foo {

    private String color;
    private int left;
    private int right;

    Foo() {

    }

    Foo(final int left, final int right) {
        this.left = left;
        this.right = right;
    }

    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(final int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(final int right) {
        this.right = right;
    }
}
