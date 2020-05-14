# Code Guidelines
## Frontend
### TS-Lint
This project uses [ts-lint](https://palantir.github.io/tslint/) to enforce most of the code guidelines. You have to follow these rules in order to contribute.
We recommend using a tslint plugin for your IDE. Intellij comes prebundled with a tslint plugin.

If your IDE does not support ts-lint, use the command ``ng lint`` to find any ts-lint errors.

### Definitions

- Type definition: a type definition looks like this: ``export type ExampleType = string;``

### General Code Guidelines
We will only cover code guidelines, which are not enforced by ts-lint here:

| Do | Don't |
|----|-------|
|Use [Angular I18n](https://angular.io/guide/i18n) to display strings or messages to the user.               | Display any string without Internationalization.|
|                                                                                                            | |
| Try to avoid using the ``any`` type. Use generics or explicit types instead.                               | Use the ``any`` type, when you know the explicit type of the object. |
|                                                                                                            | |
| Always use explicit types or define new types if necessary                                                 | Use ``object`` as a type. |
|                                                                                                            | |
| Use short types and create type definitions when a type becomes to long.                                   | Use types, that are larger than the identifier and it's type parameters. |
|                                                                                                            | |
| Add new type definitions to the typings.d.ts file, when they are globally used.                            | Put new global type definitions anywhere else but the typings.d.ts file. |
|                                                                                                            | |
| Add component scoped or service scoped type definitions in the same file, as the class that depends on it. | Add component scoped or service scoped type definitions in the typings.d.ts file. |
|                                                                                                            | |

### Architecture
If your component or service is used in...
- ... every module, declare it in the core module.
- ... some modules, declare it in the shared module.
- ... one module, declare it in this module.

# Backend
TODO

## Shared
TODO
