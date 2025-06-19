package wayoftime.bloodmagic.common.routing;

public interface IResourceMatcher<T> {

    boolean doesResourceMatch(T resource);

    int getCount();

    void setCount(int count);

    void grow(int amount);

    void shrink(int amount);

    boolean isEmpty();
}
