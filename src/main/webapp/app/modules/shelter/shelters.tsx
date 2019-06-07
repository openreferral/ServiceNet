import './shelters.scss';

import React from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { IPaginationBaseState } from 'react-jhipster';
import { connect } from 'react-redux';

import { IRootState } from 'app/shared/reducers';

export interface IShelterProp extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Shelters extends React.Component<IShelterProp, IPaginationBaseState> {
  render() {
    return <div>Shelters</div>;
  }
}

const mapStateToProps = (storeState: IRootState) => ({});

const mapDispatchToProps = {};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Shelters);
