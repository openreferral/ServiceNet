import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './required-document.reducer';
import { IRequiredDocument } from 'app/shared/model/required-document.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRequiredDocumentProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class RequiredDocument extends React.Component<IRequiredDocumentProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { requiredDocumentList, match } = this.props;
    return (
      <div>
        <h2 id="required-document-heading">
          <Translate contentKey="serviceNetApp.requiredDocument.home.title">Required Documents</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.requiredDocument.home.createLabel">Create new Required Document</Translate>
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
                  <Translate contentKey="serviceNetApp.requiredDocument.document">Document</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.requiredDocument.srvc">Srvc</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.requiredDocument.externalDbId" />
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.requiredDocument.providerName" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {requiredDocumentList.map((requiredDocument, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${requiredDocument.id}`} color="link" size="sm">
                      {requiredDocument.id}
                    </Button>
                  </td>
                  <td>{requiredDocument.document}</td>
                  <td>{requiredDocument.externalDbId}</td>
                  <td>{requiredDocument.providerName}</td>
                  <td>
                    {requiredDocument.srvcName ? <Link to={`service/${requiredDocument.srvcId}`}>{requiredDocument.srvcName}</Link> : ''}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${requiredDocument.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${requiredDocument.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${requiredDocument.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ requiredDocument }: IRootState) => ({
  requiredDocumentList: requiredDocument.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RequiredDocument);
