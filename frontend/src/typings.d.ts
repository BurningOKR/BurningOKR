// BASIC TYPES DEFINED IN @angular/forms + rxjs/Observable

type FormGroup = import('@angular/forms').FormGroup;
type FormArray = import('@angular/forms').FormArray;
type FormControl = import('@angular/forms').FormControl;
type AbstractControl = import('@angular/forms').AbstractControl;
type Observable<T> = import('rxjs').Observable<T>;

type STATUS = 'VALID' | 'INVALID' | 'PENDING' | 'DISABLED'; // <- I don't know why Angular Team doesn't define it https://github.com/angular/angular/blob/7.2.7/packages/forms/src/model.ts#L15-L45)
type STATUSs = STATUS | string; // <- string is added only becouse Angular base class use string insted of union type https://github.com/angular/angular/blob/7.2.7/packages/forms/src/model.ts#L196)

// OVVERRIDE TYPES WITH STRICT TYPED INTERFACES + SOME TYPE TRICKS TO COMPOSE INTERFACE (https://github.com/Microsoft/TypeScript/issues/16936)
interface AbstractControlTyped<T> extends AbstractControl {
  // BASE PROPS AND METHODS COMMON TO ALL FormControl/FormGroup/FormArray
  readonly value: T;
  valueChanges: Observable<T>;
  readonly status: STATUSs;
  statusChanges: Observable<STATUS>;

  get<V = unknown>(path: (string | number)[] | string): AbstractControlTyped<V> | null;

  setValue<V>(value: V extends T ? V : never, options?: { onlySelf?: boolean; emitEvent?: boolean }): void;

  patchValue<V>(value: V extends Partial<T> ? V : never, options?: { onlySelf?: boolean; emitEvent?: boolean }): void;

  reset<V>(value?: V extends Partial<T> ? V : never, options?: { onlySelf?: boolean; emitEvent?: boolean }): void;
}

interface FormControlTyped<T> extends FormControl {
  // COPIED FROM AbstractControlTyped<T> BECOUSE TS NOT SUPPORT MULPILE extends FormControl, AbstractControlTyped<T>
  readonly value: T;
  valueChanges: Observable<T>;
  readonly status: STATUSs;
  statusChanges: Observable<STATUS>;

  get<V = unknown>(path: (string | number)[] | string): AbstractControlTyped<V> | null;

  setValue<V>(value: V extends T ? V : never, options?: { onlySelf?: boolean; emitEvent?: boolean }): void;

  patchValue<V>(value: V extends Partial<T> ? V : never, options?: { onlySelf?: boolean; emitEvent?: boolean }): void;

  reset<V>(value?: V extends Partial<T> ? V : never, options?: { onlySelf?: boolean; emitEvent?: boolean }): void;
}

export type Controls<T> = { [P in keyof T]: AbstractControlTyped<T[P]> };

interface FormGroupTyped<T> extends FormGroup {
  // PROPS AND METHODS SPECIFIC OF FormGroup
  // controls: { [P in keyof T | string]: AbstractControlTyped<P extends keyof T ? T[P] : any> };
  controls: { [P in keyof T]: AbstractControlTyped<T[P]> };
  // COPIED FROM AbstractControlTyped<T> BECOUSE TS NOT SUPPORT MULPILE extends FormGroup, AbstractControlTyped<T>
  readonly value: T;
  valueChanges: Observable<T>;
  readonly status: STATUSs;
  statusChanges: Observable<STATUS>;

  registerControl<P extends keyof T>(name: P, control: AbstractControlTyped<T[P]>): AbstractControlTyped<T[P]>;

  registerControl<V = any>(name: string, control: AbstractControlTyped<V>): AbstractControlTyped<V>;

  addControl<P extends keyof T>(name: P, control: AbstractControlTyped<T[P]>): void;

  addControl<V = any>(name: string, control: AbstractControlTyped<V>): void;

  removeControl(name: keyof T): void;

  removeControl(name: string): void;

  setControl<P extends keyof T>(name: P, control: AbstractControlTyped<T[P]>): void;

  setControl<V = any>(name: string, control: AbstractControlTyped<V>): void;

  contains(name: keyof T): boolean;

  contains(name: string): boolean;

  get<P extends keyof T>(path: P): AbstractControlTyped<T[P]>;

  getRawValue(): T & { [disabledProp in string | number]: any };

  get<V = unknown>(path: (string | number)[] | string): AbstractControlTyped<V> | null;

  setValue<V>(value: V extends T ? V : never, options?: { onlySelf?: boolean; emitEvent?: boolean }): void;

  patchValue<V>(value: V extends Partial<T> ? V : never, options?: { onlySelf?: boolean; emitEvent?: boolean }): void;

  reset<V>(value?: V extends Partial<T> ? V : never, options?: { onlySelf?: boolean; emitEvent?: boolean }): void;
}

interface FormArrayTyped<T> extends FormArray {
  // PROPS AND METHODS SPECIFIC OF FormGroup
  controls: AbstractControlTyped<T>[];
  // COPIED FROM AbstractControlTyped<T[]> BECOUSE TS NOT SUPPORT MULPILE extends FormArray, AbastractControlTyped<T[]>
  readonly value: T[];
  valueChanges: Observable<T[]>;
  readonly status: STATUSs;
  statusChanges: Observable<STATUS>;

  at(index: number): AbstractControlTyped<T>;

  push<V = T>(ctrl: AbstractControlTyped<V>): void;

  insert<V = T>(index: number, control: AbstractControlTyped<V>): void;

  setControl<V = T>(index: number, control: AbstractControlTyped<V>): void;

  getRawValue(): T[];

  get<V = unknown>(path: (string | number)[] | string): AbstractControlTyped<V> | null;

  setValue<V>(value: V extends T[] ? V : never, options?: { onlySelf?: boolean; emitEvent?: boolean }): void;

  patchValue<V>(value: V extends Partial<T>[] ? V : never, options?: { onlySelf?: boolean; emitEvent?: boolean }): void;

  reset<V>(value?: V extends Partial<T>[] ? V : never, options?: { onlySelf?: boolean; emitEvent?: boolean }): void;
}

export type TypeOf<T> = new (...args: any[]) => T;
export type RouterParams = (string | number)[];
