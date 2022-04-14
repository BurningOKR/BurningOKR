var beschreibungtext = 'Die BROCKHAUS AG verwendet Cookies, um Ihnen ein möglichst optimales und auf Ihre Bedürfnisse zugeschnittenes Webseiten-​Erlebnis zu bieten. Dazu zählen Cookies, die für den reibungslosen Betrieb der Seite und deren Sicherheit notwendig sind sowie solche, die zu anonymen Statistikzwecken genutzt werden. Sie können selbst entscheiden, welche der Kategorien Sie erlauben möchten.';
if (window.innerWidth <= 625) {
  beschreibungtext = '';
}

var klaroConfig = {
  elementID: 'klaro',
  storageMethod: 'cookie',
  cookieName: 'klaro',
  cookieExpiresAfterDays: 365,
  privacyPolicy: '/demo/privacy-policy',
  default: false,
  mustConsent: true,
  lang: 'de',
  acceptAll: true,
  hideDeclineAll: false,
  hideLearnMore: false,
  acceptSelected: true,
  translations: {
    de: {
      acceptAll: 'Allen Cookies zustimmen',
      acceptSelected: 'Ausgewählten Cookies zustimmen',
      decline: 'Optionale Cookies ablehnen',

      consentModal: {
        description: beschreibungtext
      },
      analytics: {
        title: 'Matomo',
      },
      authentication: {
        title: 'Authentifizierung',
      },
      purposes: {
        analytics: '',
        authentication: '',
        video: 'Externe Videos'
      }
    }
  },

  apps: [
    {
      name: 'authentication',
      title: 'Authentifizierung',
      description: 'Authentifizierung für automatische Anmeldung',
      default: true,
      required: true
    },
    {
      name: 'analytics',
      title: 'Matomo',
      description: 'Eine Open Source, selbst gehostete Software, um anonyme Nutzungsdaten für diese Webseite zu sammeln',
      cookies: [/^_pk_.*$/, '/'],
      required: false,
      onlyOnce: true,
    },
    {
      name: 'youtube',
      title: 'YouTube',
      purposes: ['video'],
      description: 'Videos von YouTube einbinden'
    },
  ],
};
