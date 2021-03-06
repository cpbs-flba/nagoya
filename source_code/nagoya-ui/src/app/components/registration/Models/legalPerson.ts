export const legalPerson = {
  name: {
    label: 'NAME',
    value: '',
    type: 'text',
    validation: {
      required: true
    }
  },
  commercialRegisterNumber: {
    label: 'COMMERCIAL_REGISTER_NUMBER',
    value: '',
    type: 'text',
    validation: {
      required: true
    }
  },
  taxNumber: {
    label: 'TAX_NUMBER',
    value: '',
    type: 'text',
    validation: {
      required: true
    }
  },
  email: {
    label: 'EMAIL',
    value: '',
    type: 'text',
    validation: {
      required: true
    }
  },
  password: {
    label: 'PASSWORD',
    value: '',
    type: 'password',
    validation: {
      required: true
    }
  },
  passwordConfirmation: {
    label: 'PASSWORD_CONFIRMATION',
    value: '',
    type: 'password',
    validation: {
      required: true
    }
  },
  storePrivateKey: {
    label: '',
    value: true,
    type: 'boolean',
    validation: {
      required: false
    }
  },
  termsAccepted: {
    label: '',
    value: false,
    type: 'boolean',
    validation: {
      required: true
    }
  }
};
