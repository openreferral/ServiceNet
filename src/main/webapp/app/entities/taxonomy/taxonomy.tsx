import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './taxonomy.reducer';
import { ITaxonomy } from 'app/shared/model/taxonomy.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITaxonomyProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Taxonomy extends React.Component<ITaxonomyProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { taxonomyList, match } = this.props;
    return (
      <div>
        <h2 id="taxonomy-heading">
          <Translate contentKey="serviceNetApp.taxonomy.home.title">Taxonomies</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.taxonomy.home.createLabel">Create new Taxonomy</Translate>
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
                  <Translate contentKey="serviceNetApp.taxonomy.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.taxonomy.vocabulary">Vocabulary</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.taxonomy.parent">Parent</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {taxonomyList.map((taxonomy, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${taxonomy.id}`} color="link" size="sm">
                      {taxonomy.id}
                    </Button>
                  </td>
                  <td>{taxonomy.name}</td>
                  <td>{taxonomy.vocabulary}</td>
                  <td>{taxonomy.parentName ? <Link to={`taxonomy/${taxonomy.parentId}`}>{taxonomy.parentName}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${taxonomy.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${taxonomy.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${taxonomy.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ taxonomy }: IRootState) => ({
  taxonomyList: taxonomy.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Taxonomy);
