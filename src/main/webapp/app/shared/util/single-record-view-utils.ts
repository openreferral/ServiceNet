export const getTextField = (object, fieldName) => ({
  type: 'text',
  fieldName,
  defaultValue: object[fieldName]
});
