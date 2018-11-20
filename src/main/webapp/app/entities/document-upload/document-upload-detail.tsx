import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './document-upload.reducer';
import { IDocumentUpload } from 'app/shared/model/document-upload.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDocumentUploadDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class DocumentUploadDetail extends React.Component<IDocumentUploadDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { documentUploadEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.documentUpload.detail.title">DocumentUpload</Translate> [<b>{documentUploadEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="dateUploaded">
                <Translate contentKey="serviceNetApp.documentUpload.dateUploaded">Date Uploaded</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={documentUploadEntity.dateUploaded} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="documentId">
                <Translate contentKey="serviceNetApp.documentUpload.documentId">Document Id</Translate>
              </span>
            </dt>
            <dd>{documentUploadEntity.documentId}</dd>
          </dl>
          <Button tag={Link} to="/entity/document-upload" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/document-upload/${documentUploadEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ documentUpload }: IRootState) => ({
  documentUploadEntity: documentUpload.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DocumentUploadDetail);
