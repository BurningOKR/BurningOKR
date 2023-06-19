import { LoggerConfig, NgxLoggerLevel } from 'ngx-logger';

export const loggerConfig: LoggerConfig = {
  serverLoggingUrl: '/api/log',
  level: NgxLoggerLevel.TRACE,
  serverLogLevel: NgxLoggerLevel.WARN,
  enableSourceMaps: true,
};
