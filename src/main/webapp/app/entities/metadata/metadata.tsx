import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { byteSize, Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './metadata.reducer';
import { IMetadata } from 'app/shared/model/metadata.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMetadataProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Metadata extends React.Component<IMetadataProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { metadataList, match } = this.props;
    return (
      <div>
        <h2 id="metadata-heading">
          <Translate contentKey="serviceNetApp.metadata.home.title">Metadata</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.metadata.home.createLabel">Create new Metadata</Translate>
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
                  <Translate contentKey="serviceNetApp.metadata.resourceId">Resource Id</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.metadata.lastActionDate">Last Action Date</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.metadata.lastActionType">Last Action Type</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.metadata.fieldName">Field Name</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.metadata.previousValue">Previous Value</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.metadata.replacementValue">Replacement Value</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.metadata.user">User</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {metadataList.map((metadata, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${metadata.id}`} color="link" size="sm">
                      {metadata.id}
                    </Button>
                  </td>
                  <td>{metadata.resourceId}</td>
                  <td>
                    <TextFormat type="date" value={metadata.lastActionDate} format={APP_DATE_FORMAT} />
                  </td>
                  <td>
                    <Translate contentKey={`serviceNetApp.ActionType.${metadata.lastActionType}`} />
                  </td>
                  <td>{metadata.fieldName}</td>
                  <td>{metadata.previousValue}</td>
                  <td>{metadata.replacementValue}</td>
                  <td>{metadata.userLogin ? metadata.userLogin : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${metadata.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${metadata.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${metadata.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ metadata }: IRootState) => ({
  metadataList: metadata.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Metadata);
