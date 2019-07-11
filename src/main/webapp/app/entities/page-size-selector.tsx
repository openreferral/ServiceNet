import React from 'react';
import { Dropdown, DropdownMenu, DropdownToggle, DropdownItem } from 'reactstrap';
import { Translate } from 'react-jhipster';

const PageSizeSelector = props => {
  const { dropdownOpen, itemsPerPage, toggleSelect, selectFunc } = props;
  const values = [10, 20, 50, 100];

  return (
    <Dropdown isOpen={dropdownOpen} toggle={toggleSelect}>
      <DropdownToggle caret>
        <Translate contentKey={'entity.pagination.itemsPerPage'}>Items per page</Translate>
        {itemsPerPage}
      </DropdownToggle>
      <DropdownMenu right={false}>
        <DropdownItem header>
          <Translate contentKey={'entity.pagination.header'} />
        </DropdownItem>
        <DropdownItem divider />
        {values.map((value, i) => (
          <DropdownItem value={value} onClick={selectFunc(value)} key={`selectItem${i}`}>
            {value}
          </DropdownItem>
        ))}
      </DropdownMenu>
    </Dropdown>
  );
};

export default PageSizeSelector;
