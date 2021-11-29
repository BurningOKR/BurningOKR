export const i18nMock:
  (i18nData: { id: string; description: string; value: string }) => string =
  (i18nData: { id: string; description: string; value: string }) => {
    return `correct${i18nData.id.charAt(0)
      .toUpperCase()}${i18nData.id.slice(1)}`;
  };
