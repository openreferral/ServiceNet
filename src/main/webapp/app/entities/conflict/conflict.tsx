import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './conflict.reducer';
import { IConflict } from 'app/shared/model/conflict.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IConflictProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Conflict extends React.Component<IConflictProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { conflictList, match } = this.props;
    return (
      <div>
        <h2 id="conflict-heading">
          <Translate contentKey="serviceNetApp.conflict.home.title">Conflicts</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.conflict.home.createLabel">Create new Conflict</Translate>
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
                  <Translate contentKey="serviceNetApp.conflict.currentValue">Current Value</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.conflict.currentValueDate">Current Value Date</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.conflict.offeredValue">Offered Value</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.conflict.offeredValueDate">Offered Value Date</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.conflict.fieldName">Field Name</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.conflict.entityPath">Entity Path</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.conflict.state">State</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.conflict.stateDate">State Date</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.conflict.createdDate">Created Date</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.conflict.resourceId">Resource Id</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.conflict.partnerResourceId">Partner Resource Id</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.conflict.owner">Owner</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.conflict.partner">Partner</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {conflictList.map((conflict, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${conflict.id}`} color="link" size="sm">
                      {conflict.id}
                    </Button>
                  </td>
                  <td>{conflict.currentValue}</td>
                  <td>
                    <TextFormat type="date" value={conflict.currentValueDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{conflict.offeredValue}</td>
                  <td>
                    <TextFormat type="date" value={conflict.offeredValueDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{conflict.fieldName}</td>
                  <td>{conflict.entityPath}</td>
                  <td>
                    <Translate contentKey={`serviceNetApp.ConflictStateEnum.${conflict.state}`} />
                  </td>
                  <td>
                    <TextFormat type="date" value={conflict.stateDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    <TextFormat type="date" value={conflict.createdDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{conflict.resourceId}</td>
                  <td>{conflict.partnerResourceId}</td>
                  <td>{conflict.ownerId ? <Link to={`system-account/${conflict.ownerId}`}>{conflict.ownerId}</Link> : ''}</td>
                  <td>{conflict.partnerId ? <Link to={`system-account/${conflict.partnerId}`}>{conflict.partnerId}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${conflict.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${conflict.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${conflict.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ conflict }: IRootState) => ({
  conflictList: conflict.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Conflict);
