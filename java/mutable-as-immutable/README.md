# Java에서 Mutable한 객체를 Immutable하게 쓰는 방법

- Declare the class as final so it can’t be extended.
- Make all fields private so that direct access is not allowed.
- Do not provide setter methods (methods that modify fields) for variables, so that it can not be set.
- Make all mutable fields final so that their values can be assigned only once.
- Initialize all the fields through a constructor doing the deep copy.
- Perform cloning of objects in the getter methods to return a copy rather than returning the actual object reference.
- If the instance fields include references to mutable objects, don’t allow those objects to be changed
- Don’t provide methods that modify the mutable objects.
- Don’t share references to the mutable objects. Never store references to external, mutable objects passed to the constructor. If necessary, create copies and store references to the copies. Similarly, create copies of our internal mutable objects when necessary to avoid returning the originals in our methods.
