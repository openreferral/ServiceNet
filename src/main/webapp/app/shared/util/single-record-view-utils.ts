export const getTextField = (object, fieldName) => ({
  type: 'text',
  fieldName,
  defaultValue: !!object ? object[fieldName] : ''
});
export const getTextAreaField = (object, fieldName) => ({
  type: 'textarea',
  fieldName,
  defaultValue: !!object ? object[fieldName] : ''
});
