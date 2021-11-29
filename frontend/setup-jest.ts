import 'jest-preset-angular/setup-jest';
import 'linq4js'

const WARN_SUPPRESSING_PATTERNS = [
  /Could not find Angular Material core theme/,
  /The CSV file is empty/,
  /It looks like you're using the disabled attribute with a reactive form directive/
];

const warn = console.warn;

Object.defineProperty(console, 'warn', {
  value: (...params: string[]) => {
    if (!WARN_SUPPRESSING_PATTERNS.some((pattern) => pattern.test(params[0]))) {
      warn(...params);
    }
  }
});
