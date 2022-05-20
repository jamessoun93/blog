# JDK 버전 별 ArrayList 길이 가변 확장 방법 차이

[Tistory 블로그 포스팅 바로가기](https://seunghyunson.tistory.com/26)

Java로 개발하면서 배열을 사용해야 하는 경우 Collection 프레임워크의 ArrayList 클래스를 사용할 일이 굉장히 많습니다.

배열은 고정 길이 데이터 구조라서 최초에 할당해놓은 길이를 넘어가면 직접 더 큰 크기의 새로운 배열을 만들어줘야 하는 불편함이 있는 반면, ArrayList는 새로운 데이터를 추가할 때 내부적으로 길이를 가변적으로 관리해주기 때문에 더 편리하게 사용할 수 있기 때문이죠.

그렇다면 실제로 내부에서 어떤식으로 ArrayList의 길이를 가변적으로 관리할까요?

이런 ArrayList 클래스도 개발자들이 작성한 코드이고, JDK 버전이 업되면서 기존 구현 코드의 문제점을 보완하거나 더 효율이 좋게끔 업데이트합니다.

그래서 JDK 6,7,8 버전 간 ArrayList 구현 방식에 어떤 차이가 있는지 알아봤습니다.

(아래 JDK 버전 별 코드는 잘못된 이해를 방지하고자 실제 JDK 소스코드를 그래도 첨부했으니 참고 바랍니다.)

## JDK 6 - ArrayList Implementation

```java
/**
 * Increases the capacity of this <tt>ArrayList</tt> instance, if
 * necessary, to ensure that it can hold at least the number of elements
 * specified by the minimum capacity argument.
 *
 * @param   minCapacity   the desired minimum capacity
 */
public void ensureCapacity(int minCapacity) {
    modCount++;
    int oldCapacity = elementData.length;
    if (minCapacity > oldCapacity) {
        Object oldData[] = elementData;
        int newCapacity = (oldCapacity * 3)/2 + 1;
        if (newCapacity < minCapacity)
            newCapacity = minCapacity;
        // minCapacity is usually close to size, so this is a win:
        elementData = Arrays.copyOf(elementData, newCapacity);
    }
}

/**
 * Appends the specified element to the end of this list.
 *
 * @param e element to be appended to this list
 * @return <tt>true</tt> (as specified by {@link Collection#add})
 */
public boolean add(E e) {
    ensureCapacity(size + 1);  // Increments modCount!!
    elementData[size++] = e;
    return true;
}

/**
 * Inserts the specified element at the specified position in this
 * list. Shifts the element currently at that position (if any) and
 * any subsequent elements to the right (adds one to their indices).
 *
 * @param index index at which the specified element is to be inserted
 * @param element element to be inserted
 * @throws IndexOutOfBoundsException {@inheritDoc}
 */
public void add(int index, E element) {
    rangeCheckForAdd(index);

    ensureCapacity(size+1);  // Increments modCount!!
    System.arraycopy(elementData, index, elementData, index + 1,
                        size - index);
    elementData[index] = element;
    size++;
}
```

### JDK 6 - ArrayList 구현 방식 설명

`add` 메소드의 내용에 `ensureCapacity`가 ArrayList의 확장 여부를 확인하고 작업하는 부분이기 때문에 한번 살펴보겠습니다.

1. `oldCapacity`에 기존 `elementData`의 길이 할당
2. 최소 필요 길이 `minCapacity`가 `oldCapactiy`보다 큰 경우
   1. `newCapacity`를 `oldCapacity`에 3을 곱한 뒤 2로 나눈 다음 1을 더한 값(약 **1.5배**)으로 잡는다.
   2. 만약 새로운 `newCapacity`도 `minCapacity`보다 작다면 `newCapacity`를 `minCapacity`로 치환한다.
   3. `Arrays.copyOf()`를 이용해서 기존 `elementData`를 `newCapacity`만큼 복사한다.
3. 2번의 경우가 아니라면 아무 작업을 안해도 되기 때문에 함수 종료.

JDK 6에서는 더 큰 ArrayList가 필요한 경우 곱셈과 나눗셈 연산을 통해 기존 길이에서 약 1.5배만큼의 크기로 확장한 새로운 길이의 ArrayList를 생성하여 사용하는 것을 확인할 수 있습니다.

## JDK 7 - ArrayList Implementation

```java
/**
 * Appends the specified element to the end of this list.
 *
 * @param e element to be appended to this list
 * @return <tt>true</tt> (as specified by {@link Collection#add})
 */
public boolean add(E e) {
    ensureCapacityInternal(size + 1);  // Increments modCount!!
    elementData[size++] = e;
    return true;
}

/**
 * Inserts the specified element at the specified position in this
 * list. Shifts the element currently at that position (if any) and
 * any subsequent elements to the right (adds one to their indices).
 *
 * @param index index at which the specified element is to be inserted
 * @param element element to be inserted
 * @throws IndexOutOfBoundsException {@inheritDoc}
 */
public void add(int index, E element) {
    rangeCheckForAdd(index);

    ensureCapacityInternal(size + 1);  // Increments modCount!!
    System.arraycopy(elementData, index, elementData, index + 1,
                        size - index);
    elementData[index] = element;
    size++;
}

/**
 * Increases the capacity of this <tt>ArrayList</tt> instance, if
 * necessary, to ensure that it can hold at least the number of elements
 * specified by the minimum capacity argument.
 *
 * @param   minCapacity   the desired minimum capacity
 */
public void ensureCapacity(int minCapacity) {
    if (minCapacity > 0)
        ensureCapacityInternal(minCapacity);
}

private void ensureCapacityInternal(int minCapacity) {
    modCount++;
    // overflow-conscious code
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
}

/**
 * The maximum size of array to allocate.
 * Some VMs reserve some header words in an array.
 * Attempts to allocate larger arrays may result in
 * OutOfMemoryError: Requested array size exceeds VM limit
 */
private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

/**
 * Increases the capacity to ensure that it can hold at least the
 * number of elements specified by the minimum capacity argument.
 *
 * @param minCapacity the desired minimum capacity
 */
private void grow(int minCapacity) {
    // overflow-conscious code
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1);
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    // minCapacity is usually close to size, so this is a win:
    elementData = Arrays.copyOf(elementData, newCapacity);
}

private static int hugeCapacity(int minCapacity) {
    if (minCapacity < 0) // overflow
        throw new OutOfMemoryError();
    return (minCapacity > MAX_ARRAY_SIZE) ?
        Integer.MAX_VALUE :
        MAX_ARRAY_SIZE;
}
```

### JDK 7 - ArrayList 구현 방식 설명

`add` 메소드의 내용에 `ensureCapacityInternal`를 보면 `modCount`를 늘려준 뒤 `grow` 메소드를 호출하는 것을 확인할 수 있습니다.

이 `grow` 메소드가 ArrayList의 확장 여부를 확인하고 작업하는 부분이기 때문에 한번 살펴보겠습니다.

1. `oldCapacity`에 기존 `elementData`의 길이 할당
2. 최소 필요 길이 `minCapacity`가 `oldCapactiy`보다 큰 경우
   1. `newCapacity`를 `oldCapacity`에 `oldCapacity`를 비트연산을 통해 오른쪽으로 한칸 shift한 값을 더한 뒤 저장한다.
   2. 마찬가지로 만약 새로운 `newCapacity`도 `minCapacity`보다 작다면 `newCapacity`를 `minCapacity`로 치환한다.
   3. 만약, `newCapacity`가 `MAX_ARRAY_SIZE`보다 크다면 `hugeCapacity(minCapacity)`를 호출한다.
      1. `hugeCapacity`를 통해 overflow인 경우 `OutOfMemoryError`를 던지고, overflow가 아닌 경우에는 `minCapacity`의 값에 따라 `MAX_ARRAY_SIZE` 혹은 `Integer.MAX_VALUE`로 지정한다.
   4. `Arrays.copyOf()`를 이용해서 기존 `elementData`를 `newCapacity`만큼 복사한다.

JDK 7에서는 JDK 6와 다르게 더 큰 ArrayList가 필요한 경우 곱셈과 나눗셈 연산을 통하지 않고, 더 빠르고 효율적인 비트연산을 활용하는 것을 확인할 수 있습니다.

`oldCapacity + (oldCapacity >> 1)`의 비트연산 부분을 살펴보면, `oldCapacity`의 2진수를 오른쪽으로 1만큼 shift 하여 2^1만큼 나누는 것과 동일한 연산을 수행하여 기존 길이에서 약 1.5배만큼의 크기로 확장한 새로운 ArrayList를 생성하는 것이죠.

추가로, overflow에 대한 exception handling이 추가된 것도 JDK 6와의 차이점입니다.

## JDK 8 - ArrayList Implementation

```java
/**
* Appends the specified element to the end of this list.
*
* @param e element to be appended to this list
* @return <tt>true</tt> (as specified by {@link Collection#add})
*/
public boolean add(E e) {
    ensureCapacityInternal(size + 1);  // Increments modCount!!
    elementData[size++] = e;
    return true;
}

/**
 * Inserts the specified element at the specified position in this
 * list. Shifts the element currently at that position (if any) and
 * any subsequent elements to the right (adds one to their indices).
 *
 * @param index index at which the specified element is to be inserted
 * @param element element to be inserted
 * @throws IndexOutOfBoundsException {@inheritDoc}
 */
public void add(int index, E element) {
    rangeCheckForAdd(index);

    ensureCapacityInternal(size + 1);  // Increments modCount!!
    System.arraycopy(elementData, index, elementData, index + 1,
                        size - index);
    elementData[index] = element;
    size++;
}

/**
 * Increases the capacity of this <tt>ArrayList</tt> instance, if
 * necessary, to ensure that it can hold at least the number of elements
 * specified by the minimum capacity argument.
 *
 * @param   minCapacity   the desired minimum capacity
 */
public void ensureCapacity(int minCapacity) {
    int minExpand = (elementData != EMPTY_ELEMENTDATA)
        // any size if real element table
        ? 0
        // larger than default for empty table. It's already supposed to be
        // at default size.
        : DEFAULT_CAPACITY;

    if (minCapacity > minExpand) {
        ensureExplicitCapacity(minCapacity);
    }
}

private void ensureCapacityInternal(int minCapacity) {
    if (elementData == EMPTY_ELEMENTDATA) {
        minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
    }

    ensureExplicitCapacity(minCapacity);
}

private void ensureExplicitCapacity(int minCapacity) {
    modCount++;

    // overflow-conscious code
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
}

/**
 * The maximum size of array to allocate.
 * Some VMs reserve some header words in an array.
 * Attempts to allocate larger arrays may result in
 * OutOfMemoryError: Requested array size exceeds VM limit
 */
private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

/**
 * Increases the capacity to ensure that it can hold at least the
 * number of elements specified by the minimum capacity argument.
 *
 * @param minCapacity the desired minimum capacity
 */
private void grow(int minCapacity) {
    // overflow-conscious code
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1);
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    // minCapacity is usually close to size, so this is a win:
    elementData = Arrays.copyOf(elementData, newCapacity);
}
```

### JDK 8 - ArrayList 구현 방식 설명

JDK 8은 JDK 7과 동일하게 비트연산을 통해 사이즈가 더 큰 ArrayList를 만들어줍니다.

JDK 7과의 차이는 `elementData`가 ArrayList 인스턴스 최초 생성 시 할당되는 `EMPTY_ELEMENTDATA`인 경우, 인자로 받은 `minCapacity`와 `DEFAULT_CAPACITY`의 값인 `10` 중 더 큰 수를 `minCapacity`에 할당하는 부분이 추가된 점입니다.

---

이번 기회로 정말 많이 사용하는 ArrayList의 길이를 어떻게 가변적으로 관리하는지 상세하고 살펴보았고, JDK 버전 별 새로 추가된 내용들 외에도 기존에 이미 있는 클래스들에 어떤 작업들을 하는지 알게 되었습니다.

비트연산에 대해는 알고 있었지만 이렇게 실제로 활용이 된 부분은 처음 보았는데, 이 역시 이론으로만 배웠던 내용을 실제 사례로 보고 그 효율성을 확인할 수 있는 좋은 기회였던 것 같습니다.

특히, 내부 소스코드는 이해하기 어렵다는 두려움이 있었는데 열어서 확인해보니 실제로 그렇지도 않아 앞으로 항상 이런 식으로 깊이 공부하는 버릇을 들일 생각입니다.
