export const ACTION_TYPES = {
  UPDATE_SHELTER_FILTER: 'filterShelter/UPDATE_SHELTER_FILTER'
};

export const initialState = {
  loading: false,
  errorMessage: null,
  shelterFilter: { definedCoverageAreas: [], tags: [], showOnlyAvailableBeds: false }
};

export type FilterShelterState = Readonly<typeof initialState>;

export default (state: FilterShelterState = initialState, action): FilterShelterState => {
  switch (action.type) {
    case ACTION_TYPES.UPDATE_SHELTER_FILTER:
      return {
        ...state,
        shelterFilter: action.payload,
        loading: false
      };
    default:
      return state;
  }
};

// tslint:disable-next-line:ter-arrow-body-style
export const updateShelterFilter = shelterFilter => {
  return {
    type: ACTION_TYPES.UPDATE_SHELTER_FILTER,
    payload: shelterFilter
  };
};
