export const getTextField = (object, fieldName) => ({
  type: 'text',
  fieldName,
  defaultValue: object[fieldName]
});
export const getTextAreaField = (object, fieldName) => ({
  type: 'textarea',
  fieldName,
  defaultValue: object[fieldName]
});
