import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './eligibility.reducer';
import { IEligibility } from 'app/shared/model/eligibility.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IEligibilityProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Eligibility extends React.Component<IEligibilityProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { eligibilityList, match } = this.props;
    return (
      <div>
        <h2 id="eligibility-heading">
          <Translate contentKey="serviceNetApp.eligibility.home.title">Eligibilities</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.eligibility.home.createLabel">Create new Eligibility</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.eligibility.eligibility">Eligibility</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.eligibility.srvc">Srvc</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {eligibilityList.map((eligibility, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${eligibility.id}`} color="link" size="sm">
                      {eligibility.id}
                    </Button>
                  </td>
                  <td>{eligibility.eligibility}</td>
                  <td>{eligibility.srvcName ? <Link to={`service/${eligibility.srvcId}`}>{eligibility.srvcName}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${eligibility.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${eligibility.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${eligibility.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ eligibility }: IRootState) => ({
  eligibilityList: eligibility.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Eligibility);
