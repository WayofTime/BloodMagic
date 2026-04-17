Here's a quick-and-dirty style-guide for contributing code to the mod. Thank you for your interest!

Make use of newlines when lines get long, like with builder style code
If you have to scroll to see what's going on, you are probably better off splitting it, e.g:
```
return BlockBehaviour.Properties.of()
		.forceSolidOn()
		.noOcclusion()
		.strength(1.5F)
		.pushReaction(PushReaction.DESTROY)
		.mapColor(color)
		.sound(type)
		.lightLevel(state -> light);
```
A sort-of exception to this is registering stuff. we get what its doing and if we really need to know the specifics we can go look at them still. Besides, 50% of it is boilerplate anyway. e.g:
```
public static final DeferredHolder<Block, WhateverThingBlock> SOME_BLOCK_VAR_NAME = BLOCKS.register(...
```
As you can see above, we prefer tabs over spaces - please configure your IDE accordingly.

Prefer early return rather than putting everything inside an if. Do this even for a single condition.  

This:
```
if (!conditionA) {
	return;
}
if (!conditionB) {
	return;
}
doStuff();
```
Not this:
```
if (conditionA) {
	if (conditionB) {
		doStuff();
	}
}
```
Comments should not describe what the code does, but rather why it does that. Unless it's really complicated and *also* needs explaining what it does (but if you think your code is too complicated, try and simplify it! -wrince). If you need an essay, link it to a GitHub issue. 

If there is something that needs to be looked at later for whatever reason 
`// TODO {whatever it is}` goes above the line (or at the end if its short)


Example rundown of a class:
```
package whatever;

// All imports are grouped together (and usually sorted in alphabetical order)
import a;
import b;

// ... Except java internal stuff, which should be a separate group below
import java.whatever;

// Class names (and record, enums etc count too) in PascalCase
// Begin interfaces with I if they are for a capability. leave it be if its not
// Enums dont *require* Enum in their name either, but it can be helpful in some cases.
public class Name {

	// Class wide variables used all over the place go on top and should 
	// be grouped in some way that makes sense
	// Public static final's (constants) ARE_ALL_CAPS_WITH_UNDERSCORE
	public static final int CONSTANT_VAR = 5;
	public static final int CONSTANT_OTHER = 7;

	// Static vars can either be snake_case or camelCase
	// Registry Keys, Resource Locations, Translation keys and all other sorts of strings that refer to something in code should be snake_case.
	private static String some_text = "blah";

	// Booleans start with "is" or "has" or "do", or something similar 
	// This should hint at its true/false nature in context
	private boolean isGreen = false;

	public Name() {
		...
	}

	// If there's a variable that mostly just interacts with one method 
	// it can be declared just before that method (putting it with 
	// the rest on top is also fine)
	private int fieldVar = 24;
	private void doSomething() {
		if (condition) {
			doStuff();
		} else { // Else has both braces on the same line. 
		         // there is nothing gained by doing this in 2 or 3 lines
			doSomethingElse();
		}
		doMore(fieldVar);
	}

	// Setters and getters are named like this and go near where its relevant, or at the bottom
	public void setFieldVar(int fieldVar) {
		this.fieldVar = fieldVar;
	}
}
```
