import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './physical-address-fields-value.reducer';
import { IPhysicalAddressFieldsValue } from 'app/shared/model/physical-address-fields-value.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPhysicalAddressFieldsValueProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class PhysicalAddressFieldsValue extends React.Component<IPhysicalAddressFieldsValueProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { physicalAddressFieldsValueList, match } = this.props;
    return (
      <div>
        <h2 id="physical-address-fields-value-heading">
          <Translate contentKey="serviceNetApp.physicalAddressFieldsValue.home.title">Physical Address Fields Values</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.physicalAddressFieldsValue.home.createLabel">
              Create a new Physical Address Fields Value
            </Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {physicalAddressFieldsValueList && physicalAddressFieldsValueList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="serviceNetApp.physicalAddressFieldsValue.physicalAddressField">Physical Address Field</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {physicalAddressFieldsValueList.map((physicalAddressFieldsValue, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${physicalAddressFieldsValue.id}`} color="link" size="sm">
                        {physicalAddressFieldsValue.id}
                      </Button>
                    </td>
                    <td>
                      <Translate contentKey={`serviceNetApp.PhysicalAddressFields.${physicalAddressFieldsValue.physicalAddressField}`} />
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${physicalAddressFieldsValue.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${physicalAddressFieldsValue.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${physicalAddressFieldsValue.id}/delete`} color="danger" size="sm">
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
          ) : (
            <div className="alert alert-warning">
              <Translate contentKey="serviceNetApp.physicalAddressFieldsValue.home.notFound">
                No Physical Address Fields Values found
              </Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ physicalAddressFieldsValue }: IRootState) => ({
  physicalAddressFieldsValueList: physicalAddressFieldsValue.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PhysicalAddressFieldsValue);
