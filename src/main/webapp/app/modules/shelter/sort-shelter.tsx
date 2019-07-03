import React from 'react';
import { Dropdown, DropdownMenu, DropdownToggle, DropdownItem } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const SortShelter = props => {
  const values = props.values;
  const dropdownOpen = props.dropdownOpen;
  const toggleSort = props.toggleSort;
  const sort = props.sort;
  const sortFunc = props.sortFunc;

  return (
    <Dropdown isOpen={dropdownOpen} toggle={toggleSort}>
      <DropdownToggle color="primary" caret>
        <Translate contentKey="serviceNetApp.shelter.home.sort.toggle" />
      </DropdownToggle>
      <DropdownMenu right={false}>
        {values.map((value, i) => (
          <DropdownItem onClick={sortFunc(value)} key={`sortItem${i}`}>
            <Translate contentKey={`serviceNetApp.shelter.home.sort.${value}`} />{' '}
            {sort === value ? <FontAwesomeIcon icon="angle-up" /> : null}
          </DropdownItem>
        ))}
      </DropdownMenu>
    </Dropdown>
  );
};

export default SortShelter;
