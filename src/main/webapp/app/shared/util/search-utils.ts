import { Storage } from 'react-jhipster';

const DEFAULT_SORT_ORDER = 'desc';
const SORT_RECENTLY_UPDATED = 'recent';
const SORT_RECOMMENDED = 'recommended';
export const SORT_ARRAY = [SORT_RECENTLY_UPDATED, SORT_RECOMMENDED];

const defaultSearchPreferences = {
  sort: SORT_RECENTLY_UPDATED,
  order: DEFAULT_SORT_ORDER,
  searchPhrase: ''
};

export const getSearchPreferences = username => {
  if (!username) {
    return defaultSearchPreferences;
  }

  return Storage.local.get(username, defaultSearchPreferences);
};

export const resetSearchPreferences = username => {
  Storage.local.set(username, defaultSearchPreferences);
};

export const setSort = (username, sort) => {
  const searchPreferences = Storage.local.get(username, defaultSearchPreferences);
  Storage.local.set(username, { ...searchPreferences, sort });
};

export const setSearchPhrase = (username, searchPhrase) => {
  const searchPreferences = Storage.local.get(username, defaultSearchPreferences);
  Storage.local.set(username, { ...searchPreferences, searchPhrase });
};
