import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './document-upload.reducer';
import { IDocumentUpload } from 'app/shared/model/document-upload.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDocumentUploadProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class DocumentUpload extends React.Component<IDocumentUploadProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { documentUploadList, match } = this.props;
    return (
      <div>
        <h2 id="document-upload-heading">
          <Translate contentKey="serviceNetApp.documentUpload.home.title">Document Uploads</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.documentUpload.home.createLabel">Create new Document Upload</Translate>
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
                  <Translate contentKey="serviceNetApp.documentUpload.dateUploaded">Date Uploaded</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.documentUpload.documentId">Document Id</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {documentUploadList.map((documentUpload, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${documentUpload.id}`} color="link" size="sm">
                      {documentUpload.id}
                    </Button>
                  </td>
                  <td>
                    <TextFormat type="date" value={documentUpload.dateUploaded} format={APP_DATE_FORMAT} />
                  </td>
                  <td>{documentUpload.documentId}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${documentUpload.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${documentUpload.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${documentUpload.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ documentUpload }: IRootState) => ({
  documentUploadList: documentUpload.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DocumentUpload);
